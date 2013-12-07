/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import data.Objet;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.ArrayList;

/**
 *
 * @author Romain
 */
public class MiseAJourMarge extends TickerBehaviour {

    public MiseAJourMarge(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        // Catalogue du vendeur
        ArrayList<Objet> catalogue = (ArrayList<Objet>) ((VendeurAgent) this.myAgent).getCatalogue();
        
        for(Objet objet : catalogue) {
            ((VendeurAgent) this.myAgent).getVendeur().setMargeSelonStrategie(objet);
        }
    }

}
