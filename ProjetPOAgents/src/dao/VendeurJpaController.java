/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.NonexistentEntityException;
import data.Vendeur;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Romain
 */
public class VendeurJpaController implements Serializable {

    public VendeurJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vendeur vendeur) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vendeur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vendeur vendeur) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vendeur = em.merge(vendeur);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vendeur.getIdVendeur();
                if (findVendeur(id) == null) {
                    throw new NonexistentEntityException("The vendeur with id " + id + " no longer exists.");
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
            Vendeur vendeur;
            try {
                vendeur = em.getReference(Vendeur.class, id);
                vendeur.getIdVendeur();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vendeur with id " + id + " no longer exists.", enfe);
            }
            em.remove(vendeur);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vendeur> findVendeurEntities() {
        return findVendeurEntities(true, -1, -1);
    }

    public List<Vendeur> findVendeurEntities(int maxResults, int firstResult) {
        return findVendeurEntities(false, maxResults, firstResult);
    }

    private List<Vendeur> findVendeurEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Vendeur as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Vendeur findVendeur(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vendeur.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendeurCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Vendeur as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
