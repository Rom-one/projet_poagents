/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import data.Objet;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;

/**
 *
 * @author Romain
 */
public class RequeteClientProduit extends CyclicBehaviour {

    @Override
    public void action() {
        // Template destiné à identifier les requêtes pour un produit donné
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
                MessageTemplate.MatchOntology("request-one-product"));
        
        // Réception du message
        ACLMessage request = myAgent.receive(mt);
        if (request != null) {
            // Catalogue du vendeur
            ArrayList<Objet> catalogue = (ArrayList<Objet>) ((VendeurAgent) this.myAgent).getCatalogue();
            // Référence du produit demandé
            Integer produit_ref = Integer.valueOf(request.getContent());
            
            Objet produit = null;
            for (Objet objet : catalogue) {
                if (objet.getRefObjet() == produit_ref) {
                    produit = objet;
                }
            }

            int prix = produit.getPrixVente();
            // Définir la stratégie à adopter pour le prix
            
            ACLMessage reply = request.createReply();
            
            if (produit != null) {
                reply.setPerformative(ACLMessage.CONFIRM);
                reply.setOntology("reply-one-product");
                reply.setContent(String.valueOf(prix));
            } else {
                reply.setPerformative(ACLMessage.DISCONFIRM);
                reply.setOntology("reply-one-product");
            }

            myAgent.send(reply);
            
        } else {
           block();
        }

    }

}
