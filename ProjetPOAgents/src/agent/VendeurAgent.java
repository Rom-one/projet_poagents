/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import jade.core.Agent;
import java.util.HashMap;

/**
 *
 * @author Romain
 */
public class VendeurAgent extends Agent {

    private HashMap<String,String> catalogue;

    public HashMap<String, String> getCatalogue() {
        return catalogue;
    }
    
    @Override
    protected void setup() {
        System.out.println("Hello! Seller - agent " + this.getAID().getName() + " is ready");
    }
}
