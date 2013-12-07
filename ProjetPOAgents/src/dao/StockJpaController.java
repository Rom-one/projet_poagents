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
import data.Vente;
import data.Objet;
import data.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @version 0.1
 */
public class StockJpaController implements Serializable {

    public StockJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stock stock) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vente vente = stock.getVente();
            if (vente != null) {
                vente = em.getReference(vente.getClass(), vente.getIdVente());
                stock.setVente(vente);
            }
            Objet refObjet = stock.getRefObjet();
            if (refObjet != null) {
                refObjet = em.getReference(refObjet.getClass(), refObjet.getRefObjet());
                stock.setRefObjet(refObjet);
            }
            em.persist(stock);
            if (vente != null) {
                Stock oldIdStockOfVente = vente.getIdStock();
                if (oldIdStockOfVente != null) {
                    oldIdStockOfVente.setVente(null);
                    oldIdStockOfVente = em.merge(oldIdStockOfVente);
                }
                vente.setIdStock(stock);
                vente = em.merge(vente);
            }
            if (refObjet != null) {
                refObjet.getStockList().add(stock);
                refObjet = em.merge(refObjet);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stock stock) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getIdStock());
            Vente venteOld = persistentStock.getVente();
            Vente venteNew = stock.getVente();
            Objet refObjetOld = persistentStock.getRefObjet();
            Objet refObjetNew = stock.getRefObjet();
            List<String> illegalOrphanMessages = null;
            if (venteOld != null && !venteOld.equals(venteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Vente " + venteOld + " since its idStock field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (venteNew != null) {
                venteNew = em.getReference(venteNew.getClass(), venteNew.getIdVente());
                stock.setVente(venteNew);
            }
            if (refObjetNew != null) {
                refObjetNew = em.getReference(refObjetNew.getClass(), refObjetNew.getRefObjet());
                stock.setRefObjet(refObjetNew);
            }
            stock = em.merge(stock);
            if (venteNew != null && !venteNew.equals(venteOld)) {
                Stock oldIdStockOfVente = venteNew.getIdStock();
                if (oldIdStockOfVente != null) {
                    oldIdStockOfVente.setVente(null);
                    oldIdStockOfVente = em.merge(oldIdStockOfVente);
                }
                venteNew.setIdStock(stock);
                venteNew = em.merge(venteNew);
            }
            if (refObjetOld != null && !refObjetOld.equals(refObjetNew)) {
                refObjetOld.getStockList().remove(stock);
                refObjetOld = em.merge(refObjetOld);
            }
            if (refObjetNew != null && !refObjetNew.equals(refObjetOld)) {
                refObjetNew.getStockList().add(stock);
                refObjetNew = em.merge(refObjetNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stock.getIdStock();
                if (findStock(id) == null) {
                    throw new NonexistentEntityException("The stock with id " + id + " no longer exists.");
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
            Stock stock;
            try {
                stock = em.getReference(Stock.class, id);
                stock.getIdStock();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stock with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Vente venteOrphanCheck = stock.getVente();
            if (venteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Stock (" + stock + ") cannot be destroyed since the Vente " + venteOrphanCheck + " in its vente field has a non-nullable idStock field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Objet refObjet = stock.getRefObjet();
            if (refObjet != null) {
                refObjet.getStockList().remove(stock);
                refObjet = em.merge(refObjet);
            }
            em.remove(stock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stock> findStockEntities() {
        return findStockEntities(true, -1, -1);
    }

    public List<Stock> findStockEntities(int maxResults, int firstResult) {
        return findStockEntities(false, maxResults, firstResult);
    }

    private List<Stock> findStockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stock.class));
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

    public Stock findStock(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stock> rt = cq.from(Stock.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
