package agent;

import behaviour.HandlePropose;
import behaviour.HandleQueryPrice;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentParallel extends Agent {

    private static final long serialVersionUID = 1L;

    @Override
    protected void setup() {

        System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");

        //Objet obj = (Objet) objetDAO.getEntityManager().find(Objet.class, 1);
        //System.out.println(obj.getNomObjet());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("testParrallele");
        DFAgentDescription[] result = null;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException ex) {
            Logger.getLogger(VendeurAgent.class.getName()).log(Level.SEVERE, null, ex);
        }

        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("test");
        sd1.setName("testParrallele");
        dfd.addServices(sd1);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
            System.err.println("Impossible d'enregistrer les services !! ou erreur FIPA");
        }

        ParallelBehaviour comportementparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        comportementparallele.addSubBehaviour(new HandlePropose(this));
        comportementparallele.addSubBehaviour(new HandleQueryPrice(this));
        addBehaviour(comportementparallele);
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
