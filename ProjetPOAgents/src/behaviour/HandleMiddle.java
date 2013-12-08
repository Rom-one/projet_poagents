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
class HandleMiddle extends Behaviour {

    public HandleMiddle() {
    }

    @Override
    public void action() {
        System.out.println("En cours...");
    }

    @Override
    public boolean done() {
        return true;
    }

}
