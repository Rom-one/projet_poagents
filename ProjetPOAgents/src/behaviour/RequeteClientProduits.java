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
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Romain
 */
public class RequeteClientProduits extends CyclicBehaviour {

    @Override
    public void action() {
        // Template destiné à identifier les requêtes pour un produit donné
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF),
                MessageTemplate.MatchOntology("request-several-products"));

        // Réception du message
        ACLMessage request = myAgent.receive(mt);

        if (request != null) {
            // Catalogue du vendeur
            List<Objet> catalogue = ((VendeurAgent) this.myAgent).getCatalogue();
            // Produits renvoyés au client
            List<Objet> produits = new ArrayList<Objet>();
            // Filtres du produit
            String[] filters = request.getContent().split(",");

            String categorie = filters[0];
            String nom = filters[1];
            String tags = filters[2];

            for (Objet objet : catalogue) {
                // Si le produit correspond à la recherche
                if (objet.getIdCategorie().getTypeCategorie().equals(categorie) || "null".equals(categorie)
                        && (objet.getNomObjet().equals(nom) || "null".equals(nom))
                        && (objet.containsMotsCles(tags) || "null".equals(tags))) {
                    produits.add(objet);
                }
            }

            ACLMessage reply;

            if (!catalogue.isEmpty()) {
                reply = new ACLMessage(ACLMessage.CONFIRM);
                reply.setOntology("reply-several-products");
                StringBuffer content = new StringBuffer("");
                for (Objet produit : produits) {

                    // Définir stratégie pour prix
                    int prix = 42;

                    content.append(produit.getIdCategorie().getTypeCategorie())
                            .append("/")
                            .append(produit.getNomObjet())
                            .append("/")
                            .append(String.valueOf(prix))
                            .append(",");
                }
                
                reply.setContent(content.toString());
            } else {
                reply = new ACLMessage(ACLMessage.DISCONFIRM);
                reply.setOntology("reply-several-products");
            }

            myAgent.send(reply);
        } else {
            block();
        }
    }

}
