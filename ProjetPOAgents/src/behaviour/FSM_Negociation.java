/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;

/**
 *
 * @version 0.1
 */
public class FSM_Negociation extends FSMBehaviour {

    private static final long serialVersionUID = 1L;

    public FSM_Negociation(Agent agent) {
        this.myAgent = agent;

        //definiton des etats
        this.registerFirstState(new HandleInform(), "A");
        this.registerState(new HandleInform(), "B");
        this.registerState(new HandleInform(), "C");
        this.registerLastState(new HandleInform(), "D");

        //definition des transaction
        this.registerDefaultTransition("A", "B");
        this.registerTransition("B", "B", 1);
        this.registerTransition("B", "C", 0);
        this.registerTransition("C", "C", 1);
        this.registerTransition("C", "D", 0);

    }

    @Override
    public int onEnd() {
        System.out.println("FSM behaviour terminé");
        myAgent.doDelete();
        return super.onEnd();
    }

    private static class UnAutreComportement extends OneShotBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void action() {
            System.out.println("arrivée à l'etat finale");
        }
    }
}
