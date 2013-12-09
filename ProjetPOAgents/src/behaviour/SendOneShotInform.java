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
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Map;

/**
 *
 * @version 0.1
 */
class SendOneShotInform extends SimpleBehaviour {

    private static final long serialVersionUID = 1L;
    private VendeurAgent myAgent;
    private AID receiver;
    private Map.Entry<Objet, Integer> entry;
    private int end;

    public SendOneShotInform() {
    }

    SendOneShotInform(Agent agent) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
        this.end = 0;
    }

    SendOneShotInform(Agent agent, AID aid, Map.Entry<Objet, Integer> entry) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
        this.receiver = aid;
        this.entry = entry;
        this.end = 0;
    }

    @Override
    public void action() {
        if (receiver != null) {
            System.out.println("On envoi une demande (prix, quantité) au meileur enchérisseur..");
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(receiver);

            //TODO récupérer la ref du produit qu'on veut vraiment acheter
            String msgString = entry.getKey().getRefObjet().toString() + "," + entry.getValue().toString();
            msg.setContent(msgString);

            myAgent.send(msg);
            System.out.println("Requête pour la négociation d'un produit envoyé a " + receiver);
            System.out.println("Cette requete est de la forme '" + msgString + "'");
            this.end = 0;
        } else {
            this.end = 1;
        }
    }

    @Override
    public int onEnd() {
        return end;
    }

    @Override
    public boolean done() {
        return true;
    }

}
