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
class HandleBegin extends Behaviour {

    public HandleBegin() {
    }

    @Override
    public void action() {
        System.out.println("Premier State du behaviour");
    }

    @Override
    public boolean done() {
        return true;
    }

}
