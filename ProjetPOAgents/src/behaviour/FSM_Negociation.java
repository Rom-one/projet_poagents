/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @version 0.1
 */
public class FSM_Negociation extends FSMBehaviour {

    private static final long serialVersionUID = 1L;

    public FSM_Negociation(Agent agent) {
        this.myAgent = agent;

        //definiton des etats
        this.registerFirstState(new HandleBegin(), "A");
        this.registerState(new HandleMiddle(), "B");
        this.registerLastState(new HandleEnd(), "C");

        //definition des transaction
        this.registerDefaultTransition("A", "B");
        this.registerTransition("B", "C", 0);

    }

    @Override
    public int onEnd() {
        System.out.println("FSM behaviour terminé");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FSM_Negociation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.onEnd();
    }
}
