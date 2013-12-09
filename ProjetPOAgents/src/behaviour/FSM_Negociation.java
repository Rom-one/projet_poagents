/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import data.Objet;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import java.util.List;
import java.util.Map;
import utils.Proposition;

/**
 *
 * @version 0.1
 */
public class FSM_Negociation extends FSMBehaviour {

    private static final long serialVersionUID = 1L;
    private VendeurAgent vAgent;
    private List<Proposition> proposition;
    private Map.Entry<Objet, Integer> entry;

    public List<Proposition> getProposition() {
        return proposition;
    }

    public void setProposition(List<Proposition> proposition) {
        this.proposition = proposition;
    }

    FSM_Negociation(Agent agent, Map.Entry<Objet, Integer> entry) {
        super(agent);
        this.vAgent = (VendeurAgent) agent;
        this.entry = entry;
    }

    @Override
    public void onStart() {
        if (vAgent.getBestProvider() != null) {
            this.registerFirstState(new SimpleBehaviour() {

                @Override
                public void action() {
                    System.out.println("Début des négociations");
                }

                @Override
                public int onEnd() {
                    if (vAgent.getBestProvider() == null) {
                        return 1;
                    } else {
                        return 0;
                    }
                }

                @Override
                public boolean done() {
                    return true;
                }
            }, "F");
            this.registerState(new SendOneShotInform(vAgent, vAgent.getBestProvider(), entry), "A");
            this.registerState(new HandlePropose(vAgent), "B");
            this.registerLastState(new HandleAgree(vAgent), "C");
            this.registerLastState(new HandleFail(), "D");

            //definition des transaction
            this.registerTransition("F", "A", 0);
            this.registerTransition("F", "D", 1);
            this.registerTransition("A", "B", 0);
            this.registerTransition("A", "D", 1);
            this.registerTransition("B", "C", 0);
            this.registerTransition("B", "D", 1);
            super.onStart(); //To change body of generated methods, choose Tools | Templates.
        } else {
            System.out.println("Négociation inutile");
        }
    }

    @Override
    public int onEnd() {
        System.out.println("Fin des négociations");
        vAgent.setBestPrice(Double.MAX_VALUE);
        vAgent.setBestProvider(null);
        return super.onEnd();
    }
}
