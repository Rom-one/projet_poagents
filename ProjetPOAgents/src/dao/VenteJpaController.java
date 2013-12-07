/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import data.Objet;
import data.Vente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Romain
 */
public class VenteJpaController implements Serializable {

    public VenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vente vente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objet refObjet = vente.getRefObjet();
            if (refObjet != null) {
                refObjet = em.getReference(refObjet.getClass(), refObjet.getRefObjet());
                vente.setRefObjet(refObjet);
            }
            em.persist(vente);
            if (refObjet != null) {
                refObjet.getVenteList().add(vente);
                refObjet = em.merge(refObjet);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vente vente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vente persistentVente = em.find(Vente.class, vente.getIdVente());
            Objet refObjetOld = persistentVente.getRefObjet();
            Objet refObjetNew = vente.getRefObjet();
            if (refObjetNew != null) {
                refObjetNew = em.getReference(refObjetNew.getClass(), refObjetNew.getRefObjet());
                vente.setRefObjet(refObjetNew);
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
            Query q = em.createQuery("select object(o) from Vente as o");
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
            Query q = em.createQuery("select count(o) from Vente as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
