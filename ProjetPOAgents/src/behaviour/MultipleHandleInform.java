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

/**
 *
 * @version 0.1
 */
class MultipleHandleInform extends ParallelBehaviour {

    private static final long serialVersionUID = 1L;

    public MultipleHandleInform(Agent myAgent) {
        // On veut que tout les fournisseur répondent à notre demande WHEN_ALL donc..
        super(myAgent, WHEN_ALL);

        //On veut trouver tout les agent fournisseurs
        AID[] providers = ((VendeurAgent) myAgent).searchProvider();

        // Données qu'utiliseront les subBehaviour pour partager l'informations du meilleur prix de fournisseur
        ((VendeurAgent) myAgent).setBestProvider(null);
        this.getParent().getDataStore().put("betterPrice", Double.MAX_VALUE);
        for (AID aid : providers) {
            addSubBehaviour(new HandleInform(myAgent, aid));
        }

    }
}
