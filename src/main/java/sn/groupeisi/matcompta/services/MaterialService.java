package sn.groupeisi.matcompta.services;

import org.slf4j.*;
import jakarta.persistence.*;
import sn.groupeisi.matcompta.entities.Categorie;
import sn.groupeisi.matcompta.entities.InventaireComptable;
import sn.groupeisi.matcompta.entities.Materiel;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import sn.groupeisi.matcompta.enums.RoleUser;
import sn.groupeisi.matcompta.requests.MaterielRequest;
import sn.groupeisi.matcompta.utils.JpaUtil;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class MaterialService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialService.class);

    public void addMateriel(MaterielRequest mr) {
        if (!mr.champsValides()) {
            logger.warn("Champs invalides pour le matériel");
            throw new IllegalArgumentException("Tous les champs sont obligatoires.");
        }

        if (referenceExiste(mr.getReference())) {
            logger.warn("Référence de matériel déjà utilisée : {}", mr.getReference());
            throw new IllegalArgumentException("La référence du matériel est déjà utilisée.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Categorie cat = em.find(Categorie.class, mr.getCategorieId());
            if (cat == null) {
                throw new IllegalArgumentException("Catégorie non trouvée.");
            }

            InventaireComptable inventaire = null;
            if (mr.getInventaireId() != null) {
                inventaire = em.find(InventaireComptable.class, mr.getInventaireId());
                if (inventaire == null) throw new IllegalArgumentException("Inventaire introuvable.");
            }

            Materiel materiel = Materiel.builder()
                    .libelle(mr.getLibelle())
                    .reference(mr.getReference())
                    .dateAchat(mr.getDateAchat())
                    .valeurAchat(mr.getValeurAchat())
                    .dureeAmortissement(mr.getDureeAmortissement())
                    .emplacement(mr.getEmplacement())
                    .etat(mr.getEtat())
                    .categorie(cat)
                    .inventaireComptable(inventaire)
                    .build();

            tx.begin();
            em.persist(materiel);
            tx.commit();
            logger.info("Matériel ajouté : {}", materiel.getReference());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de l'ajout du matériel", e);
            throw new RuntimeException("Erreur lors de l'ajout du matériel", e);
        } finally {
            em.close();
        }
    }

    public void modifyMateriel(Long id, MaterielRequest mr) {
        if (!mr.champsValides()) {
            logger.warn("Champs invalides pour la modification du matériel");
            throw new IllegalArgumentException("Tous les champs sont obligatoires.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Materiel materiel = em.find(Materiel.class, id);
            if (materiel == null) {
                logger.warn("Matériel introuvable pour ID {}", id);
                throw new IllegalArgumentException("Matériel introuvable.");
            }

            // Vérifier si la référence a changé et si elle est déjà utilisée
            if (!materiel.getReference().equals(mr.getReference()) && referenceExiste(mr.getReference())) {
                logger.warn("La nouvelle référence '{}' est déjà utilisée", mr.getReference());
                throw new IllegalArgumentException("La référence du matériel est déjà utilisée.");
            }

            Categorie categorie = em.find(Categorie.class, mr.getCategorieId());
            if (categorie == null) {
                logger.warn("Catégorie introuvable pour ID {}", mr.getCategorieId());
                throw new IllegalArgumentException("Catégorie introuvable.");
            }

            InventaireComptable inventaire = null;
            if (mr.getInventaireId() != null) {
                inventaire = em.find(InventaireComptable.class, mr.getInventaireId());
                if (inventaire == null) throw new IllegalArgumentException("Inventaire introuvable.");
            }

            tx.begin();
            materiel.setLibelle(mr.getLibelle());
            materiel.setReference(mr.getReference());
            materiel.setDateAchat(mr.getDateAchat());
            materiel.setValeurAchat(mr.getValeurAchat());
            materiel.setDureeAmortissement(mr.getDureeAmortissement());
            materiel.setEmplacement(mr.getEmplacement());
            materiel.setEtat(mr.getEtat());
            materiel.setCategorie(categorie);
            materiel.setInventaireComptable(inventaire);
            tx.commit();

            logger.info("Matériel modifié avec succès : {}", materiel.getReference());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la modification du matériel ID {}", id, e);
            throw new RuntimeException("Erreur lors de la modification du matériel", e);
        } finally {
            em.close();
        }
    }

    public void modifyEtatMateriel(Long id, EtatMateriel nouvelEtat) {
        if (nouvelEtat == null) {
            logger.warn("État non fourni pour la modification du matériel {}", id);
            throw new IllegalArgumentException("L'état du matériel est obligatoire.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Materiel materiel = em.find(Materiel.class, id);
            if (materiel == null) {
                logger.warn("Matériel introuvable avec ID {}", id);
                throw new IllegalArgumentException("Matériel introuvable.");
            }

            tx.begin();
            materiel.setEtat(nouvelEtat);
            tx.commit();

            logger.info("État du matériel {} mis à jour en : {}", materiel.getReference(), nouvelEtat.name());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la mise à jour de l'état du matériel {}", id, e);
            throw new RuntimeException("Erreur lors de la mise à jour de l'état du matériel", e);
        } finally {
            em.close();
        }
    }

    public boolean referenceExiste(String reference) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(m) FROM Materiel m WHERE m.reference = :ref", Long.class)
                    .setParameter("ref", reference)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<Materiel> listerMateriels() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
                        SELECT m FROM Materiel m
                        LEFT JOIN FETCH m.categorie
                        LEFT JOIN FETCH m.inventaireComptable
                    """, Materiel.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Materiel findMaterielById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Materiel mat = em.find(Materiel.class, id);
            if (mat == null) {
                logger.warn("Aucun matériel trouvé avec ID {}", id);
                throw new IllegalArgumentException("Matériel introuvable.");
            }
            return mat;
        } finally {
            em.close();
        }
    }

    public void deleteMateriel(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Materiel mat = em.find(Materiel.class, id);
            if (mat == null) {
                throw new IllegalArgumentException("Matériel introuvable.");
            }

            tx.begin();
            em.remove(mat);
            tx.commit();
            logger.info("Matériel supprimé : {}", mat.getReference());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la suppression", e);
            throw new RuntimeException("Erreur lors de la suppression du matériel", e);
        } finally {
            em.close();
        }
    }

    public double calculerValeurResiduelle(Materiel materiel) {
        int anneesEcoulees = Period.between(materiel.getDateAchat(), LocalDate.now()).getYears();
        double perte = (materiel.getValeurAchat() / materiel.getDureeAmortissement()) * anneesEcoulees;
        return Math.max(materiel.getValeurAchat() - perte, 0);
    }

    public int countMaterielByEtat(EtatMateriel etat) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(m) FROM Materiel m WHERE m.etat = :etat", Long.class)
                    .setParameter("etat", etat)
                    .getSingleResult();

            return count.intValue();
        } finally {
            em.close();
        }
    }

    public long countTotalMateriel() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(m) FROM Materiel m", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Materiel> searchMateriel(String motCle) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT m FROM Materiel m WHERE LOWER(m.libelle) LIKE :mc OR LOWER(m.reference) LIKE :mc";
            return em.createQuery(jpql, Materiel.class)
                    .setParameter("mc", "%" + motCle.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Map<EtatMateriel, Integer> getNombreParEtat() {
        EntityManager em = JpaUtil.getEntityManager();
        Map<EtatMateriel, Integer> result = new EnumMap<>(EtatMateriel.class);
        try {
            for (EtatMateriel etat : EtatMateriel.values()) {
                Long count = em.createQuery(
                                "SELECT COUNT(m) FROM Materiel m WHERE m.etat = :etat", Long.class)
                        .setParameter("etat", etat)
                        .getSingleResult();
                result.put(etat, count.intValue());
            }
            return result;
        } finally {
            em.close();
        }
    }

    public Map<String, Integer> getNombreParCategorie() {
        EntityManager em = JpaUtil.getEntityManager();
        Map<String, Integer> result = new HashMap<>();
        try {
            List<Categorie> categories = em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
            for (Categorie cat : categories) {
                Long count = em.createQuery(
                                "SELECT COUNT(m) FROM Materiel m WHERE m.categorie.id = :id", Long.class)
                        .setParameter("id", cat.getId())
                        .getSingleResult();
                result.put(cat.getNom(), count.intValue());
            }
            return result;
        } finally {
            em.close();
        }
    }

    public List<Materiel> listerRecents() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Materiel m ORDER BY m.dateAchat DESC", Materiel.class)
                    .setMaxResults(4)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Materiel> listMaterielsDisponibles() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("""
            SELECT m FROM Materiel m
            WHERE m.id NOT IN (
                SELECT a.materiel.id FROM Affectation a
                WHERE a.dateRetour IS NULL
            )
        """, Materiel.class).getResultList();
        } finally {
            em.close();
        }
    }
}
