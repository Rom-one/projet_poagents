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
        super(myAgent, WHEN_ALL);
        AID[] providers = ((VendeurAgent) myAgent).searchProvider();
        for (int i = 0; i < providers.length; i++) {
            AID aid = providers[i];
            addSubBehaviour(new HandleInform(myAgent, aid, STATE_READY));
        }
    }

}
