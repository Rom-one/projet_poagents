/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @version 0.1
 */
public class HandlePropose extends SimpleBehaviour {

    private final static MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);

    public HandlePropose(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage aclMessage = myAgent.receive();
        if (aclMessage != null) {
            try {
                String message = aclMessage.getContent();
                System.out.println(myAgent.getLocalName() + ": I receive message Propose"
                                   + aclMessage + "with content" + message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            this.block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }

}
