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
    private MessageTemplate mtConversation;
    private MessageTemplate mt;

    int aleatoire;
    boolean finished;

    public HandleInform() {
        finished = false;
    }

    public HandleInform(Agent a, AID provider, String conversationId) {
        super(a);
        finished = false;
        mtProvider = MessageTemplate.MatchSender(provider);
        mt = MessageTemplate.and(mtpPerformative, mtProvider);
    }

    public MessageTemplate getMtConversation() {
        return mtConversation;
    }

    public void setMtConversation(MessageTemplate mtConversation) {
        this.mtConversation = mtConversation;
    }

    @Override
    public void action() {
        // TODO récupérer l'agent gagnant dans le DataStore
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            if (((VendeurAgent) myAgent).isAProvider(msg.getSender())) {
                String content = msg.getContent();

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
