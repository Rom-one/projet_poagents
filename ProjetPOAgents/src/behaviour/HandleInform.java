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
    private VendeurAgent myAgent;
    //private Integer countAttente;

    boolean finished;

    public HandleInform() {
        finished = false;
    }

    public HandleInform(Agent a, AID provider) {
        super(a);
        this.myAgent = (VendeurAgent) a;
        finished = false;
        mtProvider = MessageTemplate.MatchSender(provider);
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
        //countAttente = 0;
    }

    @Override
    public void action() {
        //On attend un message précis INFORM d'un provider préçis
        ACLMessage msg = myAgent.receive(mt);
        //if (countAttente < 3) {
        if (msg != null) {
            // On vérifie l'existence ce provider dans le DF
            String content = msg.getContent();
            double price = 0;
            double betterPrice = 0;
            if (content != null) {
                try {
                    price = Double.valueOf(content);
                    betterPrice = myAgent.getBestPrice();
                    // On regarde si le prix que nous a proposé ce founisseur est plus avantageux
                    if (price < betterPrice) {
                        myAgent.setBestProvider(msg.getSender());
                        myAgent.setBestPrice(price);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Prix reçu au mauvais format!");
                } catch (Exception e) {
                    System.err.println("Problème pour la transformation de string en double");
                }

                // Ce fournisseur nous a répondu, le Behaviour est donc fini
            }
            finished = true;
        } else {
            block(VendeurAgent.TEMPS_POUR_REPONDRE);
        }
//        } else {
//            finished = true;
//        }
//        countAttente++;
    }

    @Override
    public boolean done() {
        if (finished) {
            if (myAgent.getBestProvider() == null) {
                System.out.println("Tempis pour ce fournisseur..");
            } else if (myAgent.getBestProvider().equals(myAgent.getAID())) {
                System.out.println("J'ai reçu la réponse que j'attendais de " + myAgent.getAID());
            }
        }
        return finished;
    }
}
