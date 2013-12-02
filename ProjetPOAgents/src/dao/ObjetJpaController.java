package dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import data.Categorie;
import data.Objet;
import data.Stock;
import data.Vente;
import data.exceptions.IllegalOrphanException;
import data.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @version 0.1
 */
public class ObjetJpaController implements Serializable {

    public ObjetJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Objet objet) {
        if (objet.getVenteCollection() == null) {
            objet.setVenteCollection(new ArrayList<Vente>());
        }
        if (objet.getStockCollection() == null) {
            objet.setStockCollection(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorie idCategorie = objet.getIdCategorie();
            if (idCategorie != null) {
                idCategorie = em.getReference(idCategorie.getClass(), idCategorie.getIdCategorie());
                objet.setIdCategorie(idCategorie);
            }
            Collection<Vente> attachedVenteCollection = new ArrayList<Vente>();
            for (Vente venteCollectionVenteToAttach : objet.getVenteCollection()) {
                venteCollectionVenteToAttach = em.getReference(venteCollectionVenteToAttach.getClass(), venteCollectionVenteToAttach.getIdVente());
                attachedVenteCollection.add(venteCollectionVenteToAttach);
            }
            objet.setVenteCollection(attachedVenteCollection);
            Collection<Stock> attachedStockCollection = new ArrayList<Stock>();
            for (Stock stockCollectionStockToAttach : objet.getStockCollection()) {
                stockCollectionStockToAttach = em.getReference(stockCollectionStockToAttach.getClass(), stockCollectionStockToAttach.getIdStock());
                attachedStockCollection.add(stockCollectionStockToAttach);
            }
            objet.setStockCollection(attachedStockCollection);
            em.persist(objet);
            if (idCategorie != null) {
                idCategorie.getObjetCollection().add(objet);
                idCategorie = em.merge(idCategorie);
            }
            for (Vente venteCollectionVente : objet.getVenteCollection()) {
                Objet oldRefObjetOfVenteCollectionVente = venteCollectionVente.getRefObjet();
                venteCollectionVente.setRefObjet(objet);
                venteCollectionVente = em.merge(venteCollectionVente);
                if (oldRefObjetOfVenteCollectionVente != null) {
                    oldRefObjetOfVenteCollectionVente.getVenteCollection().remove(venteCollectionVente);
                    oldRefObjetOfVenteCollectionVente = em.merge(oldRefObjetOfVenteCollectionVente);
                }
            }
            for (Stock stockCollectionStock : objet.getStockCollection()) {
                Objet oldRefObjetOfStockCollectionStock = stockCollectionStock.getRefObjet();
                stockCollectionStock.setRefObjet(objet);
                stockCollectionStock = em.merge(stockCollectionStock);
                if (oldRefObjetOfStockCollectionStock != null) {
                    oldRefObjetOfStockCollectionStock.getStockCollection().remove(stockCollectionStock);
                    oldRefObjetOfStockCollectionStock = em.merge(oldRefObjetOfStockCollectionStock);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Objet objet) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objet persistentObjet = em.find(Objet.class, objet.getRefObjet());
            Categorie idCategorieOld = persistentObjet.getIdCategorie();
            Categorie idCategorieNew = objet.getIdCategorie();
            Collection<Vente> venteCollectionOld = persistentObjet.getVenteCollection();
            Collection<Vente> venteCollectionNew = objet.getVenteCollection();
            Collection<Stock> stockCollectionOld = persistentObjet.getStockCollection();
            Collection<Stock> stockCollectionNew = objet.getStockCollection();
            List<String> illegalOrphanMessages = null;
            for (Vente venteCollectionOldVente : venteCollectionOld) {
                if (!venteCollectionNew.contains(venteCollectionOldVente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Vente " + venteCollectionOldVente + " since its refObjet field is not nullable.");
                }
            }
            for (Stock stockCollectionOldStock : stockCollectionOld) {
                if (!stockCollectionNew.contains(stockCollectionOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockCollectionOldStock + " since its refObjet field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategorieNew != null) {
                idCategorieNew = em.getReference(idCategorieNew.getClass(), idCategorieNew.getIdCategorie());
                objet.setIdCategorie(idCategorieNew);
            }
            Collection<Vente> attachedVenteCollectionNew = new ArrayList<Vente>();
            for (Vente venteCollectionNewVenteToAttach : venteCollectionNew) {
                venteCollectionNewVenteToAttach = em.getReference(venteCollectionNewVenteToAttach.getClass(), venteCollectionNewVenteToAttach.getIdVente());
                attachedVenteCollectionNew.add(venteCollectionNewVenteToAttach);
            }
            venteCollectionNew = attachedVenteCollectionNew;
            objet.setVenteCollection(venteCollectionNew);
            Collection<Stock> attachedStockCollectionNew = new ArrayList<Stock>();
            for (Stock stockCollectionNewStockToAttach : stockCollectionNew) {
                stockCollectionNewStockToAttach = em.getReference(stockCollectionNewStockToAttach.getClass(), stockCollectionNewStockToAttach.getIdStock());
                attachedStockCollectionNew.add(stockCollectionNewStockToAttach);
            }
            stockCollectionNew = attachedStockCollectionNew;
            objet.setStockCollection(stockCollectionNew);
            objet = em.merge(objet);
            if (idCategorieOld != null && !idCategorieOld.equals(idCategorieNew)) {
                idCategorieOld.getObjetCollection().remove(objet);
                idCategorieOld = em.merge(idCategorieOld);
            }
            if (idCategorieNew != null && !idCategorieNew.equals(idCategorieOld)) {
                idCategorieNew.getObjetCollection().add(objet);
                idCategorieNew = em.merge(idCategorieNew);
            }
            for (Vente venteCollectionNewVente : venteCollectionNew) {
                if (!venteCollectionOld.contains(venteCollectionNewVente)) {
                    Objet oldRefObjetOfVenteCollectionNewVente = venteCollectionNewVente.getRefObjet();
                    venteCollectionNewVente.setRefObjet(objet);
                    venteCollectionNewVente = em.merge(venteCollectionNewVente);
                    if (oldRefObjetOfVenteCollectionNewVente != null && !oldRefObjetOfVenteCollectionNewVente.equals(objet)) {
                        oldRefObjetOfVenteCollectionNewVente.getVenteCollection().remove(venteCollectionNewVente);
                        oldRefObjetOfVenteCollectionNewVente = em.merge(oldRefObjetOfVenteCollectionNewVente);
                    }
                }
            }
            for (Stock stockCollectionNewStock : stockCollectionNew) {
                if (!stockCollectionOld.contains(stockCollectionNewStock)) {
                    Objet oldRefObjetOfStockCollectionNewStock = stockCollectionNewStock.getRefObjet();
                    stockCollectionNewStock.setRefObjet(objet);
                    stockCollectionNewStock = em.merge(stockCollectionNewStock);
                    if (oldRefObjetOfStockCollectionNewStock != null && !oldRefObjetOfStockCollectionNewStock.equals(objet)) {
                        oldRefObjetOfStockCollectionNewStock.getStockCollection().remove(stockCollectionNewStock);
                        oldRefObjetOfStockCollectionNewStock = em.merge(oldRefObjetOfStockCollectionNewStock);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = objet.getRefObjet();
                if (findObjet(id) == null) {
                    throw new NonexistentEntityException("The objet with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objet objet;
            try {
                objet = em.getReference(Objet.class, id);
                objet.getRefObjet();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objet with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Vente> venteCollectionOrphanCheck = objet.getVenteCollection();
            for (Vente venteCollectionOrphanCheckVente : venteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Objet (" + objet + ") cannot be destroyed since the Vente " + venteCollectionOrphanCheckVente + " in its venteCollection field has a non-nullable refObjet field.");
            }
            Collection<Stock> stockCollectionOrphanCheck = objet.getStockCollection();
            for (Stock stockCollectionOrphanCheckStock : stockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Objet (" + objet + ") cannot be destroyed since the Stock " + stockCollectionOrphanCheckStock + " in its stockCollection field has a non-nullable refObjet field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categorie idCategorie = objet.getIdCategorie();
            if (idCategorie != null) {
                idCategorie.getObjetCollection().remove(objet);
                idCategorie = em.merge(idCategorie);
            }
            em.remove(objet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Objet> findObjetEntities() {
        return findObjetEntities(true, -1, -1);
    }

    public List<Objet> findObjetEntities(int maxResults, int firstResult) {
        return findObjetEntities(false, maxResults, firstResult);
    }

    private List<Objet> findObjetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objet.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Objet findObjet(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Objet.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjetCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objet> rt = cq.from(Objet.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
