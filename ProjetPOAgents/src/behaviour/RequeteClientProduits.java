/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package behaviour;

import agent.VendeurAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.HashMap;

/**
 *
 * @author Romain
 */
public class RequeteClientProduits extends CyclicBehaviour {

    @Override
    public void action() {
        // Template destiné à identifier les requêtes pour un produit donné
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
                                                 MessageTemplate.MatchOntology("request-one-product"));
        
        // Réception du message
        ACLMessage request = myAgent.receive(mt);
        
        if(request != null) {
            // Catalogue du vendeur
            HashMap<String,String> catalogue = ((VendeurAgent) this.myAgent).getCatalogue();
            // Référence du produit demandé
            String produit_ref = request.getContent();
            ACLMessage reply;
            
            if(catalogue.containsKey(produit_ref)) {
               reply = new ACLMessage(ACLMessage.CONFIRM); 
               reply.setOntology("reply-one-product");
               reply.setContent("price");
            }
            else {
               reply = new ACLMessage(ACLMessage.DISCONFIRM); 
               reply.setOntology("reply-one-product");
            }
            
            myAgent.send(reply);
        }
        else {
            block();
        }
    }
    
}
