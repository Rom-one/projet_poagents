/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;
import utils.Proposition;

/**
 *
 * @version 0.1
 */
public class HandlePropose extends SimpleBehaviour {

    private MessageTemplate mtpPerformative = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
    private MessageTemplate mtProvider;
    private MessageTemplate mt;
    private boolean finished;
    private VendeurAgent vAgent;
//    private Integer countOfRestart;
    private Integer end;

    public HandlePropose(Agent agent) {
        super(agent);
        this.vAgent = (VendeurAgent) agent;
        finished = false;
        mtProvider = MessageTemplate.MatchSender(vAgent.getBestProvider());
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
//        countOfRestart = 0;
        end = 0;
    }

    @Override
    public void action() {
        //if (countOfRestart < 2) {
        ACLMessage aclMessage = myAgent.receive(mt);
        if (aclMessage != null) {
            try {
                String message = aclMessage.getContent();
                //TODO Traiter la proposition pour l'accepter, pour l'instant, toujours oui
                if (Proposition.isAcceptable(vAgent.getTresorerie(), message)) {
                    ACLMessage msgTosend = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    msgTosend.addReceiver(aclMessage.getSender());
                    msgTosend.setContent(message);
                    vAgent.send(msgTosend);
                    System.out.println(vAgent.getLocalName() + ": I received message Propose"
                                       + aclMessage + "with content" + message + " and I send an ACCEPT");
                    List<Proposition> propositions = Proposition.getListProposition(message);
                    ((FSM_Negociation) ((this.getParent()))).setProposition(propositions);
                } else {
                    ACLMessage msgTosend = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    msgTosend.addReceiver(aclMessage.getSender());
                    String m;
                    m = Proposition.getPropositionString(message, vAgent.getTresorerie());
                    msgTosend.setContent(m);
                    vAgent.send(msgTosend);
                    System.out.println(vAgent.getLocalName() + ": I received message Propose"
                                       + aclMessage + "with content" + message + " and I send an REJECT");
                    List<Proposition> propositions = Proposition.getListProposition(m);
                    ((FSM_Negociation) ((this.getParent()))).setProposition(propositions);
                    end = 1;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finished = true;
        } else {
            //this.block(VendeurAgent.TEMPS_POUR_REPONDRE);
            this.block();
        }
//        } else {
//            finished = true;
//            end = 1;
//        }
//        countOfRestart++;
    }

    @Override
    public int onEnd() {
        return end;
    }

    @Override
    public boolean done() {
        return finished;
    }

}
