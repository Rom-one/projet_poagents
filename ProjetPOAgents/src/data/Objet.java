package data;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @NamedQuery(name = "Objet.findByMarge", query = "SELECT o FROM Objet o WHERE o.marge = :marge")})
public class Objet implements Serializable {

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
    private int marge;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refObjet")
    private Collection<Vente> venteCollection;
    @JoinColumn(name = "idCategorie", referencedColumnName = "idCategorie")
    @ManyToOne
    private Categorie idCategorie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refObjet")
    private Collection<Stock> stockCollection;

    public Objet() {
    }

    public Objet(Integer refObjet) {
        this.refObjet = refObjet;
    }

    public Objet(Integer refObjet, String nomObjet, int marge) {
        this.refObjet = refObjet;
        this.nomObjet = nomObjet;
        this.marge = marge;
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

    public int getMarge() {
        return marge;
    }

    public void setMarge(int marge) {
        this.marge = marge;
    }

    @XmlTransient
    public Collection<Vente> getVenteCollection() {
        return venteCollection;
    }

    public void setVenteCollection(Collection<Vente> venteCollection) {
        this.venteCollection = venteCollection;
    }

    public Categorie getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Categorie idCategorie) {
        this.idCategorie = idCategorie;
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
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

}
