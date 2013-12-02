/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import dao.ObjetJpaController;
import data.Objet;
import data.Vendeur;
import jade.core.Agent;
import java.util.HashMap;
import javax.persistence.Persistence;

/**
 *
 * @author Romain
 */
public class VendeurAgent extends Agent {


    private final static int STOCK_MOYEN = 15000;
    private final static int TRESORIE_MOYENNE = 15000;
    
    private HashMap<Integer, Objet> catalogue;
    private Vendeur vendeur;
    ObjetJpaController ojc = new ObjetJpaController(Persistence.createEntityManagerFactory("POAgentPU"));

    public HashMap<Integer, Objet> getCatalogue() {
        return catalogue;
    }
    
    public Vendeur getVendeur() {
        return vendeur;
    }
    
    @Override
    protected void setup() {
        
        int stock = STOCK_MOYEN;
        int tresorie = TRESORIE_MOYENNE;
        
        vendeur = new Vendeur(tresorie,stock);
        
        //Acheter stock de départ:
        // Pour chaque produit
            // Rechercher tous les fournisseurs proposant ce produit
            // Puis Achter ce produit au fournisseur
        
        System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");
        Objet obj = (Objet) ojc.getEntityManager().find(Objet.class, 1);
        System.out.println(obj.getNomObjet());
    }
}
