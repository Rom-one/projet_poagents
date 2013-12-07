/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @version 0.1
 */
public class DemandePrixProduitFournisseur extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("fournisseur", AID.ISLOCALNAME));
        msg.setLanguage("French");
        msg.setContent("2");
        myAgent.send(msg);

        ACLMessage receivemsg = myAgent.receive();
        if (msg != null) {
            //TODO process
        }
    }

}
