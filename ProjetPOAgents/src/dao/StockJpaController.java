/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import data.Objet;
import data.Stock;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

/**
 *
 * @author Romain
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
            Objet refObjet = stock.getRefObjet();
            if (refObjet != null) {
                refObjet = em.getReference(refObjet.getClass(), refObjet.getRefObjet());
                stock.setRefObjet(refObjet);
            }
            em.persist(stock);
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

    public void edit(Stock stock) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getIdStock());
            Objet refObjetOld = persistentStock.getRefObjet();
            Objet refObjetNew = stock.getRefObjet();
            if (refObjetNew != null) {
                refObjetNew = em.getReference(refObjetNew.getClass(), refObjetNew.getRefObjet());
                stock.setRefObjet(refObjetNew);
            }
            stock = em.merge(stock);
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
            Query q = em.createQuery("select object(o) from Stock as o");
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
            Query q = em.createQuery("select count(o) from Stock as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getTotalObjetEnStock() {
        int res = 0;
        List<Objet> objetList = DAOFactory.getObjetDAO().findObjetEntities();
        for (Objet objet : objetList) {
            res += DAOFactory.getObjetDAO().getStockRestant(objet);
        }
        return res;
    }

    public void tronquer() {
        EntityManager em = getEntityManager();
        try {
            Class c = getEntityManager().getClass();
            Query q = em.createQuery("DELETE FROM " + c.getSimpleName());
            int executeUpdate = q.executeUpdate();
        } catch (Exception e) {
            System.err.println("erreur de DELETE table from Stock");
        } finally {
            em.close();
        }
    }

}
