/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package behaviour;

import agent.VendeurAgent;
import dao.DAOFactory;
import data.Objet;
import data.Vente;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Date;

/**
 *
 * @author Romain
 */
public class AchatClientProduit extends CyclicBehaviour {

    @Override
    public void action() {
        // Template destiné à identifier les requêtes pour un produit donné
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                MessageTemplate.MatchOntology("buy-product"));

        // Réception du message
        ACLMessage request = myAgent.receive(mt);
        
        if (request != null) {
            // Produit acheté et son prix
            String [] buffer = request.getContent().split(",");
            Objet objet = DAOFactory.getObjetDAO().findObjet(Integer.valueOf(buffer[0]));
            Integer prixVente = Integer.valueOf(buffer[1]);
            String acheteur = request.getSender().toString();
            Date dateVente = new Date();
            
            // Enregistrement de la vente
            Vente vente = new Vente(dateVente, prixVente, acheteur, objet);
            DAOFactory.getVenteDAO().create(vente);
            
            // Enregistrement du paiement
            ((VendeurAgent) this.myAgent).getVendeur().addRecette(prixVente);
        } else {
            block();
        }
    }
    
}
