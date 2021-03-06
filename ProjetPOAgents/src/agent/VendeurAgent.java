package agent;

import behaviour.AchatClientProduit;
import behaviour.MiseAJourMarge;
import behaviour.RequeteClientProduit;
import behaviour.RequeteClientProduits;
import behaviour.SearchBetterProviderBehaviour;
import dao.DAOFactory;
import data.Objet;
import data.Stock;
import data.Vendeur;
import data.Vente;
import jade.core.AID;
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

/**
 *
 * @author Romain
 */
public class VendeurAgent extends Agent {

    public final static int STOCK_MOYEN = 15000;
    public final static int TRESORIE_MOYENNE = 15000;

    private final static int SEMAINE_MS = 30000;
    private final static long TEMPS_DEPART = System.currentTimeMillis();// - 5 * SEMAINE_MS;
    public static final int TEMPS_POUR_REPONDRE = 30000;

    private final static String CUSTOMER_SERVICE_TYPE = "customer";
    private final static String PROVIDER_SERVICE_TYPE = "provider";
    private final static String BUSINESS_SERVICE_TYPE = "business";
    private static final long serialVersionUID = 1L;

    private List<Objet> catalogue;
    private Vendeur vendeur;
    private AID bestProvider = null;
    private Double bestPrice;
    private AID[] providersInDeal;
    private Integer tresorerie;

    public Integer getTresorerie() {
        return tresorerie;
    }

    public void setTresorerie(Integer tresorerie) {
        this.tresorerie = tresorerie;
    }

    public AID[] getProvidersInDeal() {
        return providersInDeal;
    }

    public void setProvidersInDeal(AID[] providersInDeal) {
        this.providersInDeal = providersInDeal;
    }

    public Double getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(Double bestPrice) {
        this.bestPrice = bestPrice;
    }

    public AID getBestProvider() {
        return bestProvider;
    }

    public void setBestProvider(AID bestProvider) {
        this.bestProvider = bestProvider;
    }

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
        return (int) ((date.getTime() - TEMPS_DEPART) / (SEMAINE_MS * 7 * 24 * 60 * 60));
    }

    public static Date getDate(int semaine) {
        Timestamp start = new Timestamp(TEMPS_DEPART);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());
        cal.add(Calendar.SECOND, 60 * 60 * 24 * 7 * semaine);
        return cal.getTime();
    }

    public static boolean isSoldes(int semaine) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate(semaine));
        return (cal.get(Calendar.MONTH) == Calendar.JANUARY || cal.get(Calendar.MONTH) == Calendar.FEBRUARY);
    }

    public VendeurAgent() {
        this.tresorerie = TRESORIE_MOYENNE;
        this.bestPrice = Double.MAX_VALUE;
        this.bestProvider = null;
    }

    public VendeurAgent(Integer tresorerie) {
        this.tresorerie = tresorerie;
        this.bestPrice = Double.MAX_VALUE;
        this.bestProvider = null;
    }

    @Override
    protected void setup() {
        // Initialisation du stock et de la trésorie
        int stock = STOCK_MOYEN;

        // Initialisation du vendeur
        vendeur = new Vendeur(this.tresorerie, stock);

        //Acheter stock de départ:
        // Pour chaque produit
        // Rechercher tous les fournisseurs proposant ce produit
        // Puis Acheter ce produit au fournisseur
        //genererStocks();
        //genererVentes();
        // Enregistrement du service de vente d'objets
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType(BUSINESS_SERVICE_TYPE);
        sd.setName("Vente de produits");
        dfd.addServices(sd);

        sd = new ServiceDescription();
        sd.setType(BUSINESS_SERVICE_TYPE);
        sd.setName("Negociations avec le fournisseur");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
            System.err.println("Impossible d'enregistrer les services !! ou erreur FIPA");
        }

        // Enregistrement des comportements client
        addBehaviour(new RequeteClientProduit());
        addBehaviour(new AchatClientProduit());
        addBehaviour(new RequeteClientProduits());
        // Comportement avec fournisseur
        addBehaviour(new SearchBetterProviderBehaviour(this));
        // Comportement mettant à jour la marge de chaque objet toutes les semaines
        addBehaviour(new MiseAJourMarge(this, SEMAINE_MS));
    }

    @Override
    protected void takeDown() {
        try {

            // Désinscrire le service de l'agent
            DFService.deregister(this);
            System.out.println("Vendeur Agent " + getAID().getName() + " terminated.");
        } catch (FIPAException ex) {
            Logger.getLogger(VendeurAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void genererStocks() {
        for (Objet objet : getCatalogue()) {
            long time = System.currentTimeMillis() - (3600 * 24 * 7 * 5);
            Stock stock = new Stock(new Date(time), new Date(time), 38, 50, objet);
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

    public AID[] searchProvider() {
        DFAgentDescription dfd = new DFAgentDescription();
        providersInDeal = null;

        ServiceDescription sd = new ServiceDescription();
        sd.setType(PROVIDER_SERVICE_TYPE);
        dfd.addServices(sd);
        DFAgentDescription[] result;
        try {
            result = DFService.search(this, dfd);
            providersInDeal = new AID[result.length];
            for (int i = 0; i < result.length; i++) {
                if (!(result[i].getName().equals(getAID()))) {
                    providersInDeal[i] = result[i].getName();
                }
            }
        } catch (FIPAException ex) {
            Logger.getLogger(VendeurAgent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException np) {
            System.err.println("Aucun agents provider dans le DF");
        }
        return providersInDeal;
    }

    /*
     * On vérifie si l'agent passé en paramètre fait partie de la liste des fournisseurs
     */
    public boolean isAProvider(AID agent) {
        for (AID provider : searchProvider()) {
            if (provider.equals(agent)) {
                return true;
            }
        }
        return false;
    }
}
