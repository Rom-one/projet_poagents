/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import dao.DAOFactory;
import data.Stock;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Date;
import java.util.List;
import utils.Proposition;

/**
 *
 * @version 0.1
 */
class HandleAgree extends SimpleBehaviour {

    private MessageTemplate mtpPerformative = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
    private MessageTemplate mtProvider;
    private MessageTemplate mt;
    private boolean finished;
    private VendeurAgent vAgent;
    //private Integer countOfRestart;

    public HandleAgree(Agent agent) {
        super(agent);
        this.vAgent = (VendeurAgent) agent;
        finished = false;
        mtProvider = MessageTemplate.MatchSender(vAgent.getBestProvider());
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
        //countOfRestart = 0;
    }

    @Override
    public void action() {
        //if (countOfRestart < 2) {
        ACLMessage aclMessage = myAgent.receive(mt);
        if (aclMessage != null) {
            try {
                System.out.println(vAgent.getLocalName() + ": I receive message AGREE");
                List<Proposition> propositions = ((FSM_Negociation) ((this.getParent()))).getProposition();
                if (!propositions.isEmpty()) {
                    for (Proposition proposition : propositions) {
                        // pour chaque objet on fait une opération dans la base et sur la trésorerie
                        vAgent.setTresorerie(vAgent.getTresorerie() - (proposition.getPrixTotal()).intValue());
                        Date dateFabrication = VendeurAgent.getDate(VendeurAgent.getSemaineCourante() + proposition.getDelaiFabrication());
                        Date datePaiement = VendeurAgent.getDate(VendeurAgent.getSemaineCourante() + proposition.getDelaiPaiement());
                        DAOFactory.getStockDAO().create(new Stock(dateFabrication,
                                                                  datePaiement,
                                                                  proposition.getQuantite(),
                                                                  (int) (proposition.getPrixTotal() / proposition.getQuantite()),
                                                                  DAOFactory.getObjetDAO().findObjet(Integer.valueOf(proposition.getObjet()))));
                    }
                } else {
                    //Rollback, pas réussi à agir sur la base
                    ACLMessage msgFail = new ACLMessage(ACLMessage.FAILURE);
                    msgFail.addReceiver(aclMessage.getSender());
                    myAgent.send(msgFail);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finished = true;
        } else {
            this.block(VendeurAgent.TEMPS_POUR_REPONDRE);
        }
//        } else {
//            finished = true;
//        }
//        countOfRestart++;
    }

    @Override
    public boolean done() {
        return finished;
    }

}
