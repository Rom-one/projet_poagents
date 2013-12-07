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
import data.Categorie;
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
public class ObjetJpaController implements Serializable {

    public ObjetJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Objet objet) {
        if (objet.getStockList() == null) {
            objet.setStockList(new ArrayList<Stock>());
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
            List<Stock> attachedStockList = new ArrayList<Stock>();
            for (Stock stockListStockToAttach : objet.getStockList()) {
                stockListStockToAttach = em.getReference(stockListStockToAttach.getClass(), stockListStockToAttach.getIdStock());
                attachedStockList.add(stockListStockToAttach);
            }
            objet.setStockList(attachedStockList);
            em.persist(objet);
            if (idCategorie != null) {
                idCategorie.getObjetList().add(objet);
                idCategorie = em.merge(idCategorie);
            }
            for (Stock stockListStock : objet.getStockList()) {
                Objet oldRefObjetOfStockListStock = stockListStock.getRefObjet();
                stockListStock.setRefObjet(objet);
                stockListStock = em.merge(stockListStock);
                if (oldRefObjetOfStockListStock != null) {
                    oldRefObjetOfStockListStock.getStockList().remove(stockListStock);
                    oldRefObjetOfStockListStock = em.merge(oldRefObjetOfStockListStock);
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
            List<Stock> stockListOld = persistentObjet.getStockList();
            List<Stock> stockListNew = objet.getStockList();
            List<String> illegalOrphanMessages = null;
            for (Stock stockListOldStock : stockListOld) {
                if (!stockListNew.contains(stockListOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockListOldStock + " since its refObjet field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategorieNew != null) {
                idCategorieNew = em.getReference(idCategorieNew.getClass(), idCategorieNew.getIdCategorie());
                objet.setIdCategorie(idCategorieNew);
            }
            List<Stock> attachedStockListNew = new ArrayList<Stock>();
            for (Stock stockListNewStockToAttach : stockListNew) {
                stockListNewStockToAttach = em.getReference(stockListNewStockToAttach.getClass(), stockListNewStockToAttach.getIdStock());
                attachedStockListNew.add(stockListNewStockToAttach);
            }
            stockListNew = attachedStockListNew;
            objet.setStockList(stockListNew);
            objet = em.merge(objet);
            if (idCategorieOld != null && !idCategorieOld.equals(idCategorieNew)) {
                idCategorieOld.getObjetList().remove(objet);
                idCategorieOld = em.merge(idCategorieOld);
            }
            if (idCategorieNew != null && !idCategorieNew.equals(idCategorieOld)) {
                idCategorieNew.getObjetList().add(objet);
                idCategorieNew = em.merge(idCategorieNew);
            }
            for (Stock stockListNewStock : stockListNew) {
                if (!stockListOld.contains(stockListNewStock)) {
                    Objet oldRefObjetOfStockListNewStock = stockListNewStock.getRefObjet();
                    stockListNewStock.setRefObjet(objet);
                    stockListNewStock = em.merge(stockListNewStock);
                    if (oldRefObjetOfStockListNewStock != null && !oldRefObjetOfStockListNewStock.equals(objet)) {
                        oldRefObjetOfStockListNewStock.getStockList().remove(stockListNewStock);
                        oldRefObjetOfStockListNewStock = em.merge(oldRefObjetOfStockListNewStock);
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
            List<Stock> stockListOrphanCheck = objet.getStockList();
            for (Stock stockListOrphanCheckStock : stockListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Objet (" + objet + ") cannot be destroyed since the Stock " + stockListOrphanCheckStock + " in its stockList field has a non-nullable refObjet field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categorie idCategorie = objet.getIdCategorie();
            if (idCategorie != null) {
                idCategorie.getObjetList().remove(objet);
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
