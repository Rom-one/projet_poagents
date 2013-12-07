/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import dao.ObjetJpaController;
import dao.VenteJpaController;
import data.Objet;
import data.Vendeur;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
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
    private final static String CUSTOMER_SERVICE_TYPE = "selling";
    private final static String PROVIDER_SERVICE_TYPE = "product";
    private AID[] providerAgents;

    private List<Objet> catalogue;
    private Vendeur vendeur;

    private static final ObjetJpaController objetDAO = new ObjetJpaController(Persistence.createEntityManagerFactory("POAgentPU"));
    private static final VenteJpaController venteDAO = new VenteJpaController(Persistence.createEntityManagerFactory("POAgentPU"));

    public List<Objet> getCatalogue() {
        return catalogue;
    }

    public Vendeur getVendeur() {
        return vendeur;
    }

    public static ObjetJpaController getObjetDAO() {
        return objetDAO;
    }

    public static VenteJpaController getVenteDAO() {
        return venteDAO;
    }

    @Override
    protected void setup() {

        int stock = STOCK_MOYEN;
        int tresorie = TRESORIE_MOYENNE;

        vendeur = new Vendeur(tresorie, stock);

        //Acheter stock de départ:
        // Pour chaque produit
        // Rechercher tous les fournisseurs proposant ce produit
        // Puis Achter ce produit au fournisseur
        System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");

        //Objet obj = (Objet) objetDAO.getEntityManager().find(Objet.class, 1);
        //System.out.println(obj.getNomObjet());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(PROVIDER_SERVICE_TYPE);
        DFAgentDescription[] result = null;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException ex) {
            Logger.getLogger(VendeurAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        providerAgents = new AID[result.length];
        for (int i = 0; i < result.length; i++) {
            providerAgents[i] = result[i].getName();
        }

        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType(CUSTOMER_SERVICE_TYPE);
        sd1.setName("selling");
        dfd.addServices(sd1);

        ServiceDescription sd2 = new ServiceDescription();
        sd2.setType(PROVIDER_SERVICE_TYPE);
        sd2.setName("product");
        dfd.addServices(sd2);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
            System.err.println("Impossible d'enregistrer les services !! ou erreur FIPA");
        }

        //tick every 10 seconds, for update product margin
        addBehaviour(new TickerBehaviour(this, 20000) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onTick() {
                //TODO update product margin in comparison to previous sales
            }
        });
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Vendeur Agent " + getAID().getName() + " terminated.");
    }
}
