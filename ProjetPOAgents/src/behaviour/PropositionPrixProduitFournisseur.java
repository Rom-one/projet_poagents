/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @version 0.1
 */
public class PropositionPrixProduitFournisseur extends CyclicBehaviour {

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            //message received. Process Offers
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();

            Integer price = 2;
            if (price != null) {
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(String.valueOf(price.intValue()));
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("not-available");
            }
            myAgent.send(reply);
        } else {
            // waiting for incoming message for perform again the loop
            // !!! but all blocked behaviours are activate, so Warning, TODO be careful !!!
            block();
        }
    }

}
