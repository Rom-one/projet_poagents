/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.behaviours.SimpleBehaviour;

/**
 *
 * @version 0.1
 */
class HandleFail extends SimpleBehaviour {

    public HandleFail() {
    }

    @Override
    public void action() {
        System.out.println("Les négociations ont échoué !");
    }

    @Override
    public boolean done() {
        return true;
    }

}
