package sn.groupeisi.matcompta.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.*;
import sn.groupeisi.matcompta.entities.InventaireComptable;
import sn.groupeisi.matcompta.requests.InventaireRequest;
import sn.groupeisi.matcompta.utils.JpaUtil;

import java.time.LocalDate;
import java.util.List;

public class InventaireService {
    private static final Logger logger = LoggerFactory.getLogger(InventaireService.class);

    public void addInventaire(InventaireRequest ir) {
        if (ir.getLibelle() == null || ir.getLibelle().isBlank()) {
            logger.warn("Libellé manquant lors de la création d'un inventaire");
            throw new IllegalArgumentException("Le libellé est obligatoire.");
        }

        if (libelleExiste(ir.getLibelle())) {
            logger.warn("Libellé d'inventaire déjà utilisé : {}", ir.getLibelle());
            throw new IllegalArgumentException("Ce libellé est déjà pris.");
        }

        if (ir.getDateInventaire() != null && ir.getDateInventaire().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'inventaire ne peut pas être dans le passé.");
        }

        InventaireComptable inv = InventaireComptable.builder()
                .libelle(ir.getLibelle())
                .departement(ir.getDepartement())
                .dateInventaire(ir.getDateInventaire())
                .build();

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(inv);
            transaction.commit();
            System.out.println("DEBUG");
            logger.info("Inventaire ajouté : {}", ir.getLibelle());
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logger.error("Erreur lors de l'ajout de l'inventaire", e);
            throw new RuntimeException("Échec lors de l'ajout de l'inventaire", e);
        } finally {
            em.close();
        }
    }

    public void modifyInventaire(Long id, InventaireRequest ir) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            InventaireComptable inv = em.find(InventaireComptable.class, id);
            if (inv == null) {
                logger.warn("Inventaire introuvable avec l'ID {}", id);
                throw new IllegalArgumentException("Inventaire introuvable.");
            }

            if (!inv.getLibelle().equals(ir.getLibelle()) && libelleExiste(ir.getLibelle())) {
                logger.warn("Libellé déjà utilisé : {}", ir.getLibelle());
                throw new IllegalArgumentException("Ce libellé est déjà pris.");
            }

            if (ir.getDateInventaire() != null && ir.getDateInventaire().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La date d'inventaire ne peut pas être dans le passé.");
            }

            transaction.begin();
            inv.setLibelle(ir.getLibelle());
            inv.setDepartement(ir.getDepartement());
            inv.setDateInventaire(ir.getDateInventaire());
            transaction.commit();
            logger.info("Inventaire modifié : {}", ir.getLibelle());
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logger.error("Erreur lors de la modification de l'inventaire {}", id, e);
            throw new RuntimeException("Échec de la modification", e);
        } finally {
            em.close();
        }
    }

    public void deleteInventaire(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            InventaireComptable inv = em.find(InventaireComptable.class, id);
            if (inv == null) {
                logger.warn("Suppression échouée : inventaire introuvable {}", id);
                throw new IllegalArgumentException("Inventaire introuvable.");
            }

            transaction.begin();
            em.remove(inv);
            transaction.commit();
            logger.info("Inventaire supprimé : {}", inv.getLibelle());
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logger.error("Erreur suppression inventaire {}", id, e);
            throw new RuntimeException("Échec de suppression", e);
        } finally {
            em.close();
        }
    }

    public InventaireComptable findInventaireById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            InventaireComptable inv = em.find(InventaireComptable.class, id);
            if (inv == null) {
                logger.warn("Inventaire non trouvé avec l'ID {}", id);
                throw new IllegalArgumentException("Inventaire introuvable.");
            }
            return inv;
        } finally {
            em.close();
        }
    }

    public List<InventaireComptable> listInventaires() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT i FROM InventaireComptable i LEFT JOIN FETCH i.materiels",
                    InventaireComptable.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean libelleExiste(String libelle) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(i) FROM InventaireComptable i WHERE i.libelle = :lib", Long.class)
                    .setParameter("lib", libelle)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Erreur vérification libellé inventaire : {}", libelle, e);
            throw e;
        } finally {
            em.close();
        }
    }
}
