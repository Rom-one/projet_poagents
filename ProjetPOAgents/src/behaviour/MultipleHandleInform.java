/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.Arrays;

/**
 *
 * @version 0.1
 */
class MultipleHandleInform extends ParallelBehaviour {

    private static final long serialVersionUID = 1L;
    private VendeurAgent myAgent;

    public MultipleHandleInform(Agent agent) {
        // On veut que tout les fournisseur répondent à notre demande WHEN_ALL donc..
        super(agent, WHEN_ALL);
        this.myAgent = (VendeurAgent) agent;
        //On veut trouver tout les agent fournisseurs
        AID[] providers = this.myAgent.searchProvider();
        System.out.println("providers : " + Arrays.toString(providers));

        // Données qu'utiliseront les subBehaviour pour partager l'informations du meilleur prix de fournisseur
        myAgent.setBestProvider(null);
        myAgent.setBestPrice(Double.MAX_VALUE);
        if (providers.length != 0) {
            for (AID aid : providers) {
                addSubBehaviour(new HandleInform(myAgent, aid));
                System.out.println("ok handle crée pour " + aid.toString());
            }
        }

    }

    @Override
    public int onEnd() {
        int res = super.onEnd(); //To change body of generated methods, choose Tools | Templates.
        if (myAgent.getBestProvider() != null) {
            System.out.println("On a trouvé le prix le plus bas !! Il est proposé par " + myAgent.getBestProvider());
            System.out.println("Au prix de " + myAgent.getBestPrice());
        } else {
            System.out.println("Aucun frounisseur sur ce tour");
        }
        return res;
    }

}
