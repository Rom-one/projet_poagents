/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @version 0.1
 */
public class HandleInform extends SimpleBehaviour {

    private static final long serialVersionUID = 1L;
    private MessageTemplate mtpPerformative = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    private MessageTemplate mtProvider;
    private MessageTemplate mt;

    int aleatoire;
    boolean finished;

    public HandleInform() {
        finished = false;
    }

    public HandleInform(Agent a, AID provider) {
        super(a);
        finished = false;
        mtProvider = MessageTemplate.MatchSender(provider);
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
    }

    @Override
    public void action() {
        // On attend un message précis INFORM d'un provider préçis
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            // On vérifie l'existence ce provider dans le DF
            if (((VendeurAgent) myAgent).isAProvider(msg.getSender())) {
                String content = msg.getContent();
                double price = 0;
                double betterPrice = 0;
                try {
                    price = Double.valueOf(content);
                    betterPrice = (Double) this.getParent().getDataStore().get("betterPrice");
                } catch (NumberFormatException e) {
                    System.err.println("Prix reçu au mauvais format!");
                }

                // On regarde si le prix que nous a proposé ce founisseur est plus avantageux
                if (price < betterPrice) {
                    ((VendeurAgent) myAgent).setBestProvider(msg.getSender());
                    this.getParent().getDataStore().put("betterPrice", price);
                }
                // Ce fournisseur nous a répondu, le Behaviour est donc fini
                finished = true;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return finished;
    }
}
