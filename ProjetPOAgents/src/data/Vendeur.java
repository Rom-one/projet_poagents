/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import agent.VendeurAgent;
import dao.DAOFactory;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @version 0.1
 */
@Entity
@Table(catalog = "db_poagent", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vendeur.findAll", query = "SELECT v FROM Vendeur v"),
    @NamedQuery(name = "Vendeur.findByIdVendeur", query = "SELECT v FROM Vendeur v WHERE v.idVendeur = :idVendeur"),
    @NamedQuery(name = "Vendeur.findByTresorerie", query = "SELECT v FROM Vendeur v WHERE v.tresorerie = :tresorerie"),
    @NamedQuery(name = "Vendeur.findByStockTotal", query = "SELECT v FROM Vendeur v WHERE v.stockTotal = :stockTotal")})
public class Vendeur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idVendeur;
    @Basic(optional = false)
    @Column(nullable = false)
    private int tresorerie;
    @Basic(optional = false)
    @Column(nullable = false)
    private int stockTotal;

    public Vendeur() {
    }

    public Vendeur(Integer idVendeur) {
        this.idVendeur = idVendeur;
    }

    public Vendeur(int tresorerie, int stockTotal) {
        this.tresorerie = tresorerie;
        this.stockTotal = stockTotal;
    }

    public Vendeur(Integer idVendeur, int tresorerie, int stockTotal) {
        this.idVendeur = idVendeur;
        this.tresorerie = tresorerie;
        this.stockTotal = stockTotal;
    }

    public Integer getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(Integer idVendeur) {
        this.idVendeur = idVendeur;
    }

    public int getTresorerie() {
        return tresorerie;
    }

    public void setTresorerie(int tresorerie) {
        this.tresorerie = tresorerie;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVendeur != null ? idVendeur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vendeur)) {
            return false;
        }
        Vendeur other = (Vendeur) object;
        if ((this.idVendeur == null && other.idVendeur != null) || (this.idVendeur != null && !this.idVendeur.equals(other.idVendeur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Vendeur[ idVendeur=" + idVendeur + " ]";
    }

    public void addRecette(int montant) {
        tresorerie += montant;
    }

    public void addDepense(int montant) {
        tresorerie -= montant;
    }

    public void setMargeByStock(Objet objet) {

    }

    // Augmenter le prix pour augmenter la marge
    public void setMargeByPrix(Objet objet) {
        ArrayList<Vente> ventes = (ArrayList<Vente>) DAOFactory.getVenteDAO()
                .getEntityManager()
                .createNamedQuery("Vente.findByObjetAndSemaine")
                .setParameter("objet", objet)
                .setParameter("semaine1", VendeurAgent.getDate(VendeurAgent.getSemaineCourante() - 1))
                .setParameter("semaine2", VendeurAgent.getDate(VendeurAgent.getSemaineCourante()))
                .getResultList();

        int prix = objet.getPrixVente();

        int nbventes = ventes.size();
        int stock = DAOFactory.getObjetDAO().getStockRestant(objet);

        double pourcentage = (double) (nbventes / stock);

        // Si on est presque en rupture de stock
        if (pourcentage > 0.9) {
            prix += prix * 0.1;
            objet.setPrixVente(prix);
        } else if ((prix -= prix * 0.1) >= DAOFactory.getObjetDAO().getPrixMinimum(objet)) {
            objet.setPrixVente(prix);
        }
    }

}
