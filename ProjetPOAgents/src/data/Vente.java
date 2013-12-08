/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @version 0.1
 */
@Entity
@Table(catalog = "db_poagent", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vente.findAll", query = "SELECT v FROM Vente v"),
    @NamedQuery(name = "Vente.findByIdVente", query = "SELECT v FROM Vente v WHERE v.idVente = :idVente"),
    @NamedQuery(name = "Vente.findByDateVente", query = "SELECT v FROM Vente v WHERE v.dateVente = :dateVente"),
    @NamedQuery(name = "Vente.findByPrixVente", query = "SELECT v FROM Vente v WHERE v.prixVente = :prixVente"),
    @NamedQuery(name = "Vente.findByAcheteur", query = "SELECT v FROM Vente v WHERE v.acheteur = :acheteur"),
    @NamedQuery(name = "Vente.findByObjet", query = "SELECT v FROM Vente v WHERE v.refObjet = :objet"),
    @NamedQuery(name = "Vente.findByObjetAndSemaine", query = "SELECT v FROM Vente v WHERE v.refObjet = :objet AND v.dateVente BETWEEN :semaine1 AND :semaine2")})
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idVente;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateVente;
    private Integer prixVente;
    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    private String acheteur;
    @JoinColumn(name = "refObjet", referencedColumnName = "refObjet", nullable = false)
    @ManyToOne(optional = false)
    private Objet refObjet;

    public Vente() {
    }

    public Vente(Integer idVente) {
        this.idVente = idVente;
    }

    public Vente(Integer idVente, Date dateVente, String acheteur) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.acheteur = acheteur;
    }

    public Vente(Date date, int prixVente, String acheteur, Objet objet) {
        this.dateVente = date;
        this.acheteur = acheteur;
        this.prixVente = prixVente;
        this.refObjet = objet;
    }

    public Integer getIdVente() {
        return idVente;
    }

    public void setIdVente(Integer idVente) {
        this.idVente = idVente;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
    }

    public Integer getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(Integer prixVente) {
        this.prixVente = prixVente;
    }

    public String getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
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
        hash += (idVente != null ? idVente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vente)) {
            return false;
        }
        Vente other = (Vente) object;
        if ((this.idVente == null && other.idVente != null) || (this.idVente != null && !this.idVente.equals(other.idVente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.Vente[ idVente=" + idVente + " ]";
    }

}
