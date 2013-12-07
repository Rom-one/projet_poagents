package behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 0.1
 */
public class DemandeProduitFournisseur extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    @Override
    public void action() {
        List<Object> fournisseurs;
        fournisseurs = new ArrayList();
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (Object fournisseur : fournisseurs) {
            cfp.addReceiver((AID) fournisseur);
        }
        // id produit,prix proposé
        cfp.setContent("2,15");
        myAgent.send(cfp);
    }

}
