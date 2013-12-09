/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviour;

import agent.VendeurAgent;
import dao.DAOFactory;
import data.Objet;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import java.util.Map;

/**
 *
 * @version 0.1
 */
public class SearchBetterProviderBehaviour extends SequentialBehaviour {

    private static final long serialVersionUID = 1L;
    private VendeurAgent myAgent;

    public SearchBetterProviderBehaviour(Agent agent) {
        super(agent);
        this.myAgent = (VendeurAgent) agent;
        myAgent.searchProvider();
        Map<Objet, Integer> objetARacheter = DAOFactory.getObjetDAO().getObjetsARacheter();
        if (objetARacheter.size() == 0) {
            System.out.println("Aucun objet à racheter...");
        } else {
            System.out.println("Il y a " + objetARacheter.size() + " objet à racheter");
        }
        if (myAgent.getProvidersInDeal().length != 0) {
            for (Map.Entry<Objet, Integer> entry : objetARacheter.entrySet()) {
                Objet objet = entry.getKey();
                addSubBehaviour(new HandleQueryPrice(agent, objet));
                addSubBehaviour(new MultipleHandleInform(agent));
                addSubBehaviour(new FSM_Negociation(agent, entry));
            }
        } else {
            System.out.println("Aucun fournisseur dans les parrage, on ne peut rien acheter");
            System.out.println("Tresorerie =" + myAgent.getTresorerie());
        }
    }

    @Override
    public int onEnd() {
        myAgent.addBehaviour(new SearchBetterProviderBehaviour(myAgent));
        return super.onEnd();
    }

}
