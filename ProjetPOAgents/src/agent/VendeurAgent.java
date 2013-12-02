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

    private HashMap<String, String> catalogue;
    private Vendeur vendeur;
    ObjetJpaController ojc = new ObjetJpaController(Persistence.createEntityManagerFactory("POAgentPU"));

    public HashMap<String, String> getCatalogue() {
        return catalogue;
    }

    @Override
    protected void setup() {
        System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");
        Objet obj = (Objet) ojc.getEntityManager().find(Objet.class, 1);
        System.out.println(obj.getNomObjet());
    }
}
