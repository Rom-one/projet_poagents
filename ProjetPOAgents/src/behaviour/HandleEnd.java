/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.behaviours.Behaviour;

/**
 *
 * @version 0.1
 */
class HandleEnd extends Behaviour {

    public HandleEnd() {
    }

    @Override
    public void action() {
        System.out.println("Dernier behaviour du FSM");
    }

    @Override
    public boolean done() {
        return true;
    }

}
