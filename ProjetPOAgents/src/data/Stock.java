/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @version 0.1
 */
@Entity
@Table(catalog = "db_poagent", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s"),
    @NamedQuery(name = "Stock.findByIdStock", query = "SELECT s FROM Stock s WHERE s.idStock = :idStock"),
    @NamedQuery(name = "Stock.findByDateStockage", query = "SELECT s FROM Stock s WHERE s.dateStockage = :dateStockage"),
    @NamedQuery(name = "Stock.findByDatePaiement", query = "SELECT s FROM Stock s WHERE s.datePaiement = :datePaiement"),
    @NamedQuery(name = "Stock.findByNbVendu", query = "SELECT s FROM Stock s WHERE s.nbVendu = :nbVendu"),
    @NamedQuery(name = "Stock.findByQuantite", query = "SELECT s FROM Stock s WHERE s.quantite = :quantite"),
    @NamedQuery(name = "Stock.findByPrixAchat", query = "SELECT s FROM Stock s WHERE s.prixAchat = :prixAchat"),
    @NamedQuery(name = "Stock.findByObjetAndSemaine", query = "SELECT s FROM Stock s WHERE s.refObjet = :refObjet AND s.dateStockage BETWEEN :semaine1 AND :semaine2")})
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idStock;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStockage;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    @Basic(optional = false)
    @Column(nullable = false)
    private int nbVendu;
    @Basic(optional = false)
    @Column(nullable = false)
    private int quantite;
    @Basic(optional = false)
    @Column(nullable = false)
    private int prixAchat;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "idStock")
    private Vente vente;
    @JoinColumn(name = "refObjet", referencedColumnName = "refObjet", nullable = false)
    @ManyToOne(optional = false)
    private Objet refObjet;

    public Stock() {
    }

    public Stock(Integer idStock) {
        this.idStock = idStock;
    }

    public Stock(Integer idStock, Date dateStockage, Date datePaiement, int nbVendu, int quantite, int prixAchat) {
        this.idStock = idStock;
        this.dateStockage = dateStockage;
        this.datePaiement = datePaiement;
        this.nbVendu = nbVendu;
        this.quantite = quantite;
        this.prixAchat = prixAchat;
    }

    public Stock(Date dateStockage, Date datePaiement, int quantite, int prixAchat, Objet refObjet) {
        this.dateStockage = dateStockage;
        this.datePaiement = datePaiement;
        this.quantite = quantite;
        this.prixAchat = prixAchat;
        this.refObjet = refObjet;
    }

    public Integer getIdStock() {
        return idStock;
    }

    public void setIdStock(Integer idStock) {
        this.idStock = idStock;
    }

    public Date getDateStockage() {
        return dateStockage;
    }

    public void setDateStockage(Date dateStockage) {
        this.dateStockage = dateStockage;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public int getNbVendu() {
        return nbVendu;
    }

    public void setNbVendu(int nbVendu) {
        this.nbVendu = nbVendu;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Objet getRefObjet() {
        return refObjet;
    }

    public void setRefObjet(Objet refObjet) {
        this.refObjet = refObjet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStock != null ? idStock.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.idStock == null && other.idStock != null) || (this.idStock != null && !this.idStock.equals(other.idStock))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Stock[ idStock=" + idStock + " ]";
    }

}
