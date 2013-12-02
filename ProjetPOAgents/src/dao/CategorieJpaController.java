package dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import data.Categorie;
import data.Objet;
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
public class CategorieJpaController implements Serializable {

    public CategorieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categorie categorie) {
        if (categorie.getObjetCollection() == null) {
            categorie.setObjetCollection(new ArrayList<Objet>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Objet> attachedObjetCollection = new ArrayList<Objet>();
            for (Objet objetCollectionObjetToAttach : categorie.getObjetCollection()) {
                objetCollectionObjetToAttach = em.getReference(objetCollectionObjetToAttach.getClass(), objetCollectionObjetToAttach.getRefObjet());
                attachedObjetCollection.add(objetCollectionObjetToAttach);
            }
            categorie.setObjetCollection(attachedObjetCollection);
            em.persist(categorie);
            for (Objet objetCollectionObjet : categorie.getObjetCollection()) {
                Categorie oldIdCategorieOfObjetCollectionObjet = objetCollectionObjet.getIdCategorie();
                objetCollectionObjet.setIdCategorie(categorie);
                objetCollectionObjet = em.merge(objetCollectionObjet);
                if (oldIdCategorieOfObjetCollectionObjet != null) {
                    oldIdCategorieOfObjetCollectionObjet.getObjetCollection().remove(objetCollectionObjet);
                    oldIdCategorieOfObjetCollectionObjet = em.merge(oldIdCategorieOfObjetCollectionObjet);
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
            Collection<Objet> objetCollectionOld = persistentCategorie.getObjetCollection();
            Collection<Objet> objetCollectionNew = categorie.getObjetCollection();
            Collection<Objet> attachedObjetCollectionNew = new ArrayList<Objet>();
            for (Objet objetCollectionNewObjetToAttach : objetCollectionNew) {
                objetCollectionNewObjetToAttach = em.getReference(objetCollectionNewObjetToAttach.getClass(), objetCollectionNewObjetToAttach.getRefObjet());
                attachedObjetCollectionNew.add(objetCollectionNewObjetToAttach);
            }
            objetCollectionNew = attachedObjetCollectionNew;
            categorie.setObjetCollection(objetCollectionNew);
            categorie = em.merge(categorie);
            for (Objet objetCollectionOldObjet : objetCollectionOld) {
                if (!objetCollectionNew.contains(objetCollectionOldObjet)) {
                    objetCollectionOldObjet.setIdCategorie(null);
                    objetCollectionOldObjet = em.merge(objetCollectionOldObjet);
                }
            }
            for (Objet objetCollectionNewObjet : objetCollectionNew) {
                if (!objetCollectionOld.contains(objetCollectionNewObjet)) {
                    Categorie oldIdCategorieOfObjetCollectionNewObjet = objetCollectionNewObjet.getIdCategorie();
                    objetCollectionNewObjet.setIdCategorie(categorie);
                    objetCollectionNewObjet = em.merge(objetCollectionNewObjet);
                    if (oldIdCategorieOfObjetCollectionNewObjet != null && !oldIdCategorieOfObjetCollectionNewObjet.equals(categorie)) {
                        oldIdCategorieOfObjetCollectionNewObjet.getObjetCollection().remove(objetCollectionNewObjet);
                        oldIdCategorieOfObjetCollectionNewObjet = em.merge(oldIdCategorieOfObjetCollectionNewObjet);
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
            Collection<Objet> objetCollection = categorie.getObjetCollection();
            for (Objet objetCollectionObjet : objetCollection) {
                objetCollectionObjet.setIdCategorie(null);
                objetCollectionObjet = em.merge(objetCollectionObjet);
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
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categorie.class));
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
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categorie> rt = cq.from(Categorie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
