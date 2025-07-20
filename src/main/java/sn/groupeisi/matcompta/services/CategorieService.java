package sn.groupeisi.matcompta.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.*;
import sn.groupeisi.matcompta.entities.Categorie;
import sn.groupeisi.matcompta.requests.CategorieRequest;
import sn.groupeisi.matcompta.utils.JpaUtil;

import java.util.List;

public class CategorieService {
    private static final Logger logger = LoggerFactory.getLogger(CategorieService.class);

    public void addCategory(CategorieRequest cr) {
        if (!cr.champsValides()) {
            logger.warn("Nom de catégorie invalide ou manquant");
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire.");
        }

        if (nomExiste(cr.getNom())) {
            logger.warn("Tentative d'ajout d'une catégorie déjà existante : {}", cr.getNom());
            throw new IllegalArgumentException("Ce nom de catégorie est déjà utilisé.");
        }

        Categorie categorie = Categorie.builder()
                .nom(cr.getNom())
                .description(cr.getDescription())
                .build();

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(categorie);
            tx.commit();
            logger.info("Catégorie ajoutée avec succès : {}", cr.getNom());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de l'ajout de la catégorie", e);
            throw new RuntimeException("Erreur lors de l'ajout de la catégorie", e);
        } finally {
            em.close();
        }
    }

    public void modifyCategory(Long id, CategorieRequest cr) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Categorie categorie = em.find(Categorie.class, id);
            if (categorie == null) {
                logger.warn("Modification échouée : catégorie introuvable avec ID {}", id);
                throw new IllegalArgumentException("Catégorie introuvable.");
            }

            // Vérification si le nom est modifié et déjà utilisé par une autre
            if (!categorie.getNom().equals(cr.getNom()) && nomExiste(cr.getNom())) {
                logger.warn("Nom de catégorie déjà utilisé : {}", cr.getNom());
                throw new IllegalArgumentException("Ce nom est déjà pris.");
            }

            tx.begin();
            categorie.setNom(cr.getNom());
            categorie.setDescription(cr.getDescription());
            tx.commit();
            logger.info("Catégorie modifiée avec succès : {}", cr.getNom());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la modification de la catégorie {}", id, e);
            throw new RuntimeException("Erreur lors de la modification de la catégorie", e);
        } finally {
            em.close();
        }
    }

    public void deleteCategory(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Categorie categorie = em.find(Categorie.class, id);
            if (categorie == null) {
                logger.warn("Suppression échouée : catégorie introuvable avec ID {}", id);
                throw new IllegalArgumentException("Catégorie introuvable.");
            }

            tx.begin();
            em.remove(categorie);
            tx.commit();
            logger.info("Catégorie supprimée : {}", categorie.getNom());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la suppression de la catégorie {}", id, e);
            throw new RuntimeException("Erreur lors de la suppression", e);
        } finally {
            em.close();
        }
    }

    public Categorie findCategoryById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Categorie cat = em.find(Categorie.class, id);
            if (cat == null) {
                logger.warn("Aucune catégorie trouvée avec l'ID {}", id);
                throw new IllegalArgumentException("Catégorie introuvable.");
            }
            return cat;
        } finally {
            em.close();
        }
    }

    public List<Categorie> listerCategorys() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean nomExiste(String nom) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Categorie c WHERE c.nom = :nom", Long.class)
                    .setParameter("nom", nom)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification du nom de catégorie : {}", nom, e);
            throw e;
        } finally {
            em.close();
        }
    }

    public int countMaterielsByCategorie(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(m) FROM Materiel m WHERE m.categorie.id = :id", Long.class
            ).setParameter("id", id).getSingleResult();

            logger.info("Catégorie ID {} a {} matériels", id, count);
            return count.intValue();
        } finally {
            em.close();
        }
    }

    public List<Categorie> searchCategories(String query) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            if (query == null || query.trim().isEmpty()) {
                return em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
            }

            return em.createQuery(
                            "SELECT c FROM Categorie c WHERE LOWER(c.nom) LIKE :q OR LOWER(c.description) LIKE :q",
                            Categorie.class)
                    .setParameter("q", "%" + query.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
