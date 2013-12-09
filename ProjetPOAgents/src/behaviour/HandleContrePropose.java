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
import utils.Proposition;

/**
 *
 * @version 0.1
 */
class HandleContrePropose extends SimpleBehaviour {

    private MessageTemplate mtpPerformative = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
    private MessageTemplate mtProvider;
    private MessageTemplate mt;
    private Integer end;
    private VendeurAgent vAgent;
    private boolean finished;

    public HandleContrePropose(Agent agent) {
        this.vAgent = (VendeurAgent) agent;
        mtProvider = MessageTemplate.MatchSender(vAgent.getBestProvider());
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
        this.end = 0;
        this.finished = false;
    }

    @Override
    public void action() {
        ACLMessage aclMessage = myAgent.receive(mt);
        if (aclMessage != null) {
            try {
                String message = aclMessage.getContent();
                //TODO Traiter la proposition pour l'accepter, pour l'instant, toujours oui
                if (Proposition.isAcceptable(vAgent.getTresorerie(), message)) {
                    ACLMessage msgTosend = new ACLMessage(ACLMessage.CANCEL);
                    msgTosend.addReceiver(aclMessage.getSender());
                    vAgent.send(msgTosend);
                    System.out.println(vAgent.getLocalName() + ": I send a FAILED to " + aclMessage.getSender());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            finished = true;
        } else {
            //this.block(VendeurAgent.TEMPS_POUR_REPONDRE);
            this.block();
        }
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
