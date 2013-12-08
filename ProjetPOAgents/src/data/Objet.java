/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @version 0.1
 */
@Entity
@Table(catalog = "db_poagent", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objet.findAll", query = "SELECT o FROM Objet o"),
    @NamedQuery(name = "Objet.findByRefObjet", query = "SELECT o FROM Objet o WHERE o.refObjet = :refObjet"),
    @NamedQuery(name = "Objet.findByNomObjet", query = "SELECT o FROM Objet o WHERE o.nomObjet = :nomObjet"),
    @NamedQuery(name = "Objet.findByPrixVente", query = "SELECT o FROM Objet o WHERE o.prixVente = :prixVente")})
public class Objet implements Serializable {

    public static final double POURCENTAGE_PERTE = 0.40;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer refObjet;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String nomObjet;
    @Lob
    @Column(length = 65535)
    private String motCle;
    @Basic(optional = false)
    @Column(nullable = false)
    private int prixVente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refObjet", fetch = FetchType.LAZY)
    private List<Vente> venteList;
    @JoinColumn(name = "idCategorie", referencedColumnName = "idCategorie")
    @ManyToOne
    private Categorie idCategorie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refObjet", fetch = FetchType.LAZY)
    private List<Stock> stockList;

    public Objet() {
    }

    public Objet(Integer refObjet) {
        this.refObjet = refObjet;
    }

    public Objet(Integer refObjet, String nomObjet, int prixVente) {
        this.refObjet = refObjet;
        this.nomObjet = nomObjet;
        this.prixVente = prixVente;
    }

    public Integer getRefObjet() {
        return refObjet;
    }

    public void setRefObjet(Integer refObjet) {
        this.refObjet = refObjet;
    }

    public String getNomObjet() {
        return nomObjet;
    }

    public void setNomObjet(String nomObjet) {
        this.nomObjet = nomObjet;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    public int getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(int prixVente) {
        this.prixVente = prixVente;
    }

    @XmlTransient
    public List<Vente> getVenteList() {
        return venteList;
    }

    public void setVenteList(List<Vente> venteList) {
        this.venteList = venteList;
    }

    public Categorie getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Categorie idCategorie) {
        this.idCategorie = idCategorie;
    }

    @XmlTransient
    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (refObjet != null ? refObjet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objet)) {
            return false;
        }
        Objet other = (Objet) object;
        if ((this.refObjet == null && other.refObjet != null) || (this.refObjet != null && !this.refObjet.equals(other.refObjet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Objet[ refObjet=" + refObjet + " ]";
    }

    public boolean containsMotsCles(String buffer) {
        // Mots-clés recherchés
        String[] search_tags = buffer.split("/");
        // Mots-clés de l'objet
        String[] tags = motCle.split(",");

        int cpt = 0;
        // On recherche chaque tag dans la liste de tags de l'objet
        for (String search_tag : search_tags) {
            for (String tag : tags) {
                if (search_tag.toLowerCase().equals(tag.toLowerCase())) {
                    cpt++;
                }
            }
        }

        // Si on trouve autant de tags qu'on en recherchait, la condition est validée
        return (cpt == search_tags.length);
    }

}
