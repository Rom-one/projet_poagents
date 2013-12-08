/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @version 0.1
 */
public class SearchBetterProviderBehaviour extends SequentialBehaviour {

    private static final long serialVersionUID = 1L;
    private VendeurAgent myAgent;

    public SearchBetterProviderBehaviour(Agent agent) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
        myAgent.setBestPrice(Double.MAX_VALUE);
        myAgent.setBestProvider(null);
        addSubBehaviour(new HandleQueryPrice(agent));
        addSubBehaviour(new MultipleHandleInform(agent));
        addSubBehaviour(new FSM_Negociation(agent));
    }

    @Override
    public int onEnd() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SearchBetterProviderBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        myAgent.addBehaviour(new SearchBetterProviderBehaviour(myAgent));
        return super.onEnd();
    }

}
