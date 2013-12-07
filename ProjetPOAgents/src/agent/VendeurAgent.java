/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import behaviour.AchatClientProduit;
import behaviour.RequeteClientProduit;
import behaviour.RequeteClientProduits;
import dao.DAOFactory;
import dao.ObjetJpaController;
import dao.VenteJpaController;
import data.Objet;
import data.Stock;
import data.Vendeur;
import data.Vente;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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

    private final static int SEMAINE_MS = 1000;
    private final static long TEMPS_DEPART = System.currentTimeMillis() - 5 * SEMAINE_MS;

    private List<Objet> catalogue;
    private Vendeur vendeur;

    public List<Objet> getCatalogue() {
        return DAOFactory.getObjetDAO().findObjetEntities();
    }

    public Vendeur getVendeur() {
        return vendeur;
    }

    public static int getSemaineCourante() {
        return (int) ((System.currentTimeMillis() - TEMPS_DEPART) / SEMAINE_MS);
    }

    public static int getSemaine(Date date) {
        return (int) ((date.getTime() - TEMPS_DEPART) / SEMAINE_MS);
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
        // Puis Acheter ce produit au fournisseur
        genererStocks();
        genererVentes();

        // Enregistrement des comportements client
        addBehaviour(new RequeteClientProduit());
        addBehaviour(new AchatClientProduit());
        addBehaviour(new RequeteClientProduits());

        // Enregistrement du service de vente d'objets
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("product-selling");
        sd.setName("Vente de produits");

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

    public void genererStocks() {
        for (Objet objet : getCatalogue()) {
            long time = System.currentTimeMillis() - (3600 * 24 * 7 * 5);
            Stock stock = new Stock(new Date(time), new Date(time), 50, 50, objet);
            DAOFactory.getStockDAO().create(stock);
        }
    }

    public void genererVentes() {
        for (Objet objet : getCatalogue()) {
            for (int week = 0; week <= 5; week++) {
                long time = System.currentTimeMillis();
                Timestamp original = new Timestamp(time);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(original.getTime());
                cal.add(Calendar.SECOND, -(3600 * 24 * 7 * week));
                Timestamp later = new Timestamp(cal.getTime().getTime());
                
                int prix = (int) (55 + Math.random() * (75 - 55 + 1));
                for (int cpt = 0; cpt <= 5; cpt++) {
                    Vente vente = new Vente(new Date(later.getTime()), prix, "buyer", objet);
                    DAOFactory.getVenteDAO().create(vente);
                }
            }
        }
    }
}
