/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.NonexistentEntityException;
import data.Categorie;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import data.Objet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Romain
 */
public class CategorieJpaController implements Serializable {

    public CategorieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorie categorie) {
        if (categorie.getObjetList() == null) {
            categorie.setObjetList(new ArrayList<Objet>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Objet> attachedObjetList = new ArrayList<Objet>();
            for (Objet objetListObjetToAttach : categorie.getObjetList()) {
                objetListObjetToAttach = em.getReference(objetListObjetToAttach.getClass(), objetListObjetToAttach.getRefObjet());
                attachedObjetList.add(objetListObjetToAttach);
            }
            categorie.setObjetList(attachedObjetList);
            em.persist(categorie);
            for (Objet objetListObjet : categorie.getObjetList()) {
                Categorie oldIdCategorieOfObjetListObjet = objetListObjet.getIdCategorie();
                objetListObjet.setIdCategorie(categorie);
                objetListObjet = em.merge(objetListObjet);
                if (oldIdCategorieOfObjetListObjet != null) {
                    oldIdCategorieOfObjetListObjet.getObjetList().remove(objetListObjet);
                    oldIdCategorieOfObjetListObjet = em.merge(oldIdCategorieOfObjetListObjet);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categorie categorie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categorie persistentCategorie = em.find(Categorie.class, categorie.getIdCategorie());
            List<Objet> objetListOld = persistentCategorie.getObjetList();
            List<Objet> objetListNew = categorie.getObjetList();
            List<Objet> attachedObjetListNew = new ArrayList<Objet>();
            for (Objet objetListNewObjetToAttach : objetListNew) {
                objetListNewObjetToAttach = em.getReference(objetListNewObjetToAttach.getClass(), objetListNewObjetToAttach.getRefObjet());
                attachedObjetListNew.add(objetListNewObjetToAttach);
            }
            objetListNew = attachedObjetListNew;
            categorie.setObjetList(objetListNew);
            categorie = em.merge(categorie);
            for (Objet objetListOldObjet : objetListOld) {
                if (!objetListNew.contains(objetListOldObjet)) {
                    objetListOldObjet.setIdCategorie(null);
                    objetListOldObjet = em.merge(objetListOldObjet);
                }
            }
            for (Objet objetListNewObjet : objetListNew) {
                if (!objetListOld.contains(objetListNewObjet)) {
                    Categorie oldIdCategorieOfObjetListNewObjet = objetListNewObjet.getIdCategorie();
                    objetListNewObjet.setIdCategorie(categorie);
                    objetListNewObjet = em.merge(objetListNewObjet);
                    if (oldIdCategorieOfObjetListNewObjet != null && !oldIdCategorieOfObjetListNewObjet.equals(categorie)) {
                        oldIdCategorieOfObjetListNewObjet.getObjetList().remove(objetListNewObjet);
                        oldIdCategorieOfObjetListNewObjet = em.merge(oldIdCategorieOfObjetListNewObjet);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categorie.getIdCategorie();
                if (findCategorie(id) == null) {
                    throw new NonexistentEntityException("The categorie with id " + id + " no longer exists.");
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
            Categorie categorie;
            try {
                categorie = em.getReference(Categorie.class, id);
                categorie.getIdCategorie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categorie with id " + id + " no longer exists.", enfe);
            }
            List<Objet> objetList = categorie.getObjetList();
            for (Objet objetListObjet : objetList) {
                objetListObjet.setIdCategorie(null);
                objetListObjet = em.merge(objetListObjet);
            }
            em.remove(categorie);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categorie> findCategorieEntities() {
        return findCategorieEntities(true, -1, -1);
    }

    public List<Categorie> findCategorieEntities(int maxResults, int firstResult) {
        return findCategorieEntities(false, maxResults, firstResult);
    }

    private List<Categorie> findCategorieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Categorie as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Categorie findCategorie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categorie.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategorieCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Categorie as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
