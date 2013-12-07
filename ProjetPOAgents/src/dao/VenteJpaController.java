/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import data.Objet;
import data.Stock;
import data.Vente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @version 0.1
 */
public class VenteJpaController implements Serializable {

    public VenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vente vente) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        Stock idStockOrphanCheck = vente.getIdStock();
        if (idStockOrphanCheck != null) {
            Vente oldVenteOfIdStock = idStockOrphanCheck.getVente();
            if (oldVenteOfIdStock != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Stock " + idStockOrphanCheck + " already has an item of type Vente whose idStock column cannot be null. Please make another selection for the idStock field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objet refObjet = vente.getRefObjet();
            if (refObjet != null) {
                refObjet = em.getReference(refObjet.getClass(), refObjet.getRefObjet());
                vente.setRefObjet(refObjet);
            }
            Stock idStock = vente.getIdStock();
            if (idStock != null) {
                idStock = em.getReference(idStock.getClass(), idStock.getIdStock());
                vente.setIdStock(idStock);
            }
            em.persist(vente);
            if (refObjet != null) {
                refObjet.getVenteList().add(vente);
                refObjet = em.merge(refObjet);
            }
            if (idStock != null) {
                idStock.setVente(vente);
                idStock = em.merge(idStock);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vente vente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vente persistentVente = em.find(Vente.class, vente.getIdVente());
            Objet refObjetOld = persistentVente.getRefObjet();
            Objet refObjetNew = vente.getRefObjet();
            Stock idStockOld = persistentVente.getIdStock();
            Stock idStockNew = vente.getIdStock();
            List<String> illegalOrphanMessages = null;
            if (idStockNew != null && !idStockNew.equals(idStockOld)) {
                Vente oldVenteOfIdStock = idStockNew.getVente();
                if (oldVenteOfIdStock != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Stock " + idStockNew + " already has an item of type Vente whose idStock column cannot be null. Please make another selection for the idStock field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (refObjetNew != null) {
                refObjetNew = em.getReference(refObjetNew.getClass(), refObjetNew.getRefObjet());
                vente.setRefObjet(refObjetNew);
            }
            if (idStockNew != null) {
                idStockNew = em.getReference(idStockNew.getClass(), idStockNew.getIdStock());
                vente.setIdStock(idStockNew);
            }
            vente = em.merge(vente);
            if (refObjetOld != null && !refObjetOld.equals(refObjetNew)) {
                refObjetOld.getVenteList().remove(vente);
                refObjetOld = em.merge(refObjetOld);
            }
            if (refObjetNew != null && !refObjetNew.equals(refObjetOld)) {
                refObjetNew.getVenteList().add(vente);
                refObjetNew = em.merge(refObjetNew);
            }
            if (idStockOld != null && !idStockOld.equals(idStockNew)) {
                idStockOld.setVente(null);
                idStockOld = em.merge(idStockOld);
            }
            if (idStockNew != null && !idStockNew.equals(idStockOld)) {
                idStockNew.setVente(vente);
                idStockNew = em.merge(idStockNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vente.getIdVente();
                if (findVente(id) == null) {
                    throw new NonexistentEntityException("The vente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vente vente;
            try {
                vente = em.getReference(Vente.class, id);
                vente.getIdVente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vente with id " + id + " no longer exists.", enfe);
            }
            Objet refObjet = vente.getRefObjet();
            if (refObjet != null) {
                refObjet.getVenteList().remove(vente);
                refObjet = em.merge(refObjet);
            }
            Stock idStock = vente.getIdStock();
            if (idStock != null) {
                idStock.setVente(null);
                idStock = em.merge(idStock);
            }
            em.remove(vente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vente> findVenteEntities() {
        return findVenteEntities(true, -1, -1);
    }

    public List<Vente> findVenteEntities(int maxResults, int firstResult) {
        return findVenteEntities(false, maxResults, firstResult);
    }

    private List<Vente> findVenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vente.class));
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

    public Vente findVente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vente.class, id);
        } finally {
            em.close();
        }
    }

    public int getVenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vente> rt = cq.from(Vente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
