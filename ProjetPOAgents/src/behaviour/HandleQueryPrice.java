/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @version 0.1
 */
public class HandleQueryPrice extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;
    VendeurAgent myAgent;

    public HandleQueryPrice(Agent agent) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
    }

    @Override
    public void action() {
        AID[] providers = myAgent.getProvidersInDeal();
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        if (providers.length != 0) {
            for (AID aid : providers) {
                msg.addReceiver(aid);
                //TODO récupérer la ref du produit qu'on veut vraiment acheter
                msg.setContent("2");
                myAgent.send(msg);
                System.out.println("Requête de prix envoyé a " + aid);
            }

        }
    }

    @Override
    public int onEnd() {
        if (myAgent.getBestProvider() == null) {
            System.out.println("Donc aucun message à envoyer ");
        }
        return super.onEnd(); //To change body of generated methods, choose Tools | Templates.
    }

}
