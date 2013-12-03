/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import behaviour.AchatClientProduit;
import behaviour.RequeteClientProduit;
import behaviour.RequeteClientProduits;
import dao.ObjetJpaController;
import dao.VenteJpaController;
import data.Objet;
import data.Vendeur;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;

/**
 *
 * @author Romain
 */
public class VendeurAgent extends Agent {

    private final static int STOCK_MOYEN = 15000;
    private final static int TRESORIE_MOYENNE = 15000;
    
    private final static long TEMPS_DEPART = System.currentTimeMillis();
    private final static int SEMAINE_MS = 1000;

    private List<Objet> catalogue;
    private Vendeur vendeur;

    public List<Objet> getCatalogue() {
        return catalogue;
    }

    public Vendeur getVendeur() {
        return vendeur;
    }
    
    public int getSemaine() {
        return (int) ((System.currentTimeMillis() - TEMPS_DEPART)/SEMAINE_MS);
    }

    @Override
    protected void setup() {

        // Initialisation du stock et de la trésorie
        int stock = STOCK_MOYEN;
        int tresorie = TRESORIE_MOYENNE;

         // Initialisation du vendeur
        vendeur = new Vendeur(tresorie, stock);

        //Acheter stock de départ:
        // Pour chaque produit
        // Rechercher tous les fournisseurs proposant ce produit
        // Puis Achter ce produit au fournisseur
        /*System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");*/
        
        // Enregistrement des comportements client
        addBehaviour(new AchatClientProduit());
        addBehaviour(new RequeteClientProduit());
        addBehaviour(new RequeteClientProduits());

        // Enregistrement du service de vente d'objets
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        
        ServiceDescription sd = new ServiceDescription();
        sd.setType("product-selling");
        
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            // Désinscrire le service de l'agent
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(VendeurAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
