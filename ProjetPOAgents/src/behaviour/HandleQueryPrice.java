/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import data.Objet;
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
    private VendeurAgent myAgent;
    private Objet objet;

    public HandleQueryPrice() {
        super();
    }

    public HandleQueryPrice(Agent agent) {
        super(agent);
    }

    public HandleQueryPrice(Agent agent, Objet objet) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
        this.objet = objet;
    }

    @Override
    public void action() {
        AID[] providers = myAgent.getProvidersInDeal();
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        if (providers.length != 0) {
            for (AID aid : providers) {
                msg.addReceiver(aid);
                //TODO récupérer la ref du produit qu'on veut vraiment acheter
                msg.setContent(String.valueOf(objet.getRefObjet()));
                myAgent.send(msg);
                System.out.println("Requête de prix envoyé a " + aid + " pour l'objet de reférence " + String.valueOf(objet.getRefObjet()));
            }
        }
    }

    @Override
    public int onEnd() {
        System.out.println("tresorerie = " + myAgent.getTresorerie());
        return super.onEnd();
    }

}
