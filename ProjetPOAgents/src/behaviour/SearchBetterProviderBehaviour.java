/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

/**
 *
 * @version 0.1
 */
public class SearchBetterProviderBehaviour extends SequentialBehaviour {

    private static final long serialVersionUID = 1L;
    private double bestProvider;

    public double getBestProvider() {
        return bestProvider;
    }

    public void setBestProvider(double bestProvider) {
        this.bestProvider = bestProvider;
    }

    public SearchBetterProviderBehaviour(Agent agent) {
        super(agent);
        addSubBehaviour(new MultipleHandleInform(myAgent));
        addSubBehaviour(new HandleQueryPrice(myAgent));
    }
}
