/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 0.1
 */
public class Proposition {

    private String refObjet;
    private Integer quantite;
    private Double prixTotal;
    private Integer delaiFabrication;
    private Integer delaiPaiement;

    public Proposition(String objet, Integer quantite, Double prixTotal, Integer delaiFabrication, Integer delaiPaiement) {
        this.refObjet = objet;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
        this.delaiFabrication = delaiFabrication;
        this.delaiPaiement = delaiPaiement;
    }

    public String getObjet() {
        return refObjet;
    }

    public void setObjet(String objet) {
        this.refObjet = objet;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Integer getDelaiFabrication() {
        return delaiFabrication;
    }

    public void setDelaiFabrication(Integer delaiFabrication) {
        this.delaiFabrication = delaiFabrication;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public static boolean isASingleProposition(String proposition) {
        return !proposition.contains(";");
    }

    public static List<Proposition> getListProposition(String propositionListString) {
        List<Proposition> propositions = new ArrayList<>();
        String[] values;
        String[] propositionsString;
        Proposition p;
        if (isASingleProposition(propositionListString)) {
            propositions.add(getProposition(propositionListString));
        } else {
            values = propositionListString.split(";");
            for (String string : values) {
                propositions.add(getProposition(string));
            }

        }

        return propositions;
    }

    public static Proposition getProposition(String propositionString) {
        String[] values;
        Proposition p = null;
        values = propositionString.split(",");
        try {
            if (values.length == 5) {
                p = new Proposition(values[0], Integer.valueOf(values[1]), Double.valueOf(values[2]), Integer.valueOf(values[3]), Integer.valueOf(values[4]));
            } else {
                throw new NumberFormatException("Nombre de valeur incorrecte dans la proposition");
            }
        } catch (NumberFormatException nf) {
            System.err.println(nf.getMessage());
        }
        return p;
    }

    public static boolean isAcceptable(Integer tresorerie, String propositionString) {
        boolean acceptable = false;
        List<Proposition> propositions = null;
        propositions = getListProposition(propositionString);
        int prixTotal = 0;
        for (Proposition proposition : propositions) {
            prixTotal += proposition.getPrixTotal();
        }
        return (prixTotal < tresorerie);
    }
}
