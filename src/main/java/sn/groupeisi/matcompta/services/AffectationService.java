package sn.groupeisi.matcompta.services;

import jakarta.persistence.*;
import org.slf4j.*;
import sn.groupeisi.matcompta.entities.*;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import sn.groupeisi.matcompta.requests.AffectationRequest;
import sn.groupeisi.matcompta.requests.CategorieRequest;
import sn.groupeisi.matcompta.utils.JpaUtil;

import java.time.LocalDate;
import java.util.List;

public class AffectationService {
    private static final Logger logger = LoggerFactory.getLogger(AffectationService.class);

    public void addAffectation(AffectationRequest ar) {
        if (!ar.champsValides()) {
            logger.warn("Champs obligatoires manquants pour l'affectation");
            throw new IllegalArgumentException("Les champs obligatoires sont manquants.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Materiel materiel = em.find(Materiel.class, ar.getMaterielId());
            Utilisateur employe = em.find(Utilisateur.class, ar.getEmployeId());

            if (materiel == null || employe == null) {
                throw new IllegalArgumentException("Matériel ou utilisateur introuvable.");
            }

            Affectation affectation = Affectation.builder()
                    .materiel(materiel)
                    .employe(employe)
                    .dateAffectation(ar.getDateAffectation())
                    .dateRetour(ar.getDateRetour())
                    .etatMaterielAvant(ar.getEtatMaterielAvant())
                    .etatMaterielApres(ar.getEtatMaterielApres())
                    .commentaire(ar.getCommentaire())
                    .build();

            tx.begin();
            em.persist(affectation);
            tx.commit();

            logger.info("Affectation enregistrée pour le matériel {} à l'employé {}",
                    materiel.getReference(), employe.getEmail());

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de l'enregistrement de l'affectation", e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'affectation", e);
        } finally {
            em.close();
        }
    }

    public List<Affectation> listerAffectationsRecentes() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Affectation a " +
                                    "LEFT JOIN FETCH a.materiel " +
                                    "LEFT JOIN FETCH a.employe " +
                                    "ORDER BY a.dateAffectation DESC", Affectation.class)
                    .setMaxResults(4)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Affectation> listerToutesAffectations() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Affectation a " +
                                    "LEFT JOIN FETCH a.materiel " +
                                    "LEFT JOIN FETCH a.employe " +
                                    "ORDER BY a.dateAffectation DESC", Affectation.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Affectation findAffectationById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            logger.info("Recherche de l'affectation avec ID {}", id);
            return em.createQuery(
                            "SELECT a FROM Affectation a " +
                                    "LEFT JOIN FETCH a.materiel " +
                                    "LEFT JOIN FETCH a.employe " +
                                    "WHERE a.id = :id", Affectation.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void signerAffectation(Long idAffectation, EtatMateriel etatApres, String commentaireRetour) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Affectation affectation = em.find(Affectation.class, idAffectation);
            if (affectation == null) {
                throw new IllegalArgumentException("Affectation introuvable.");
            }

            tx.begin();
            affectation.setDateRetour(LocalDate.now());
            affectation.setEtatMaterielApres(etatApres);
            affectation.setCommentaire(commentaireRetour);
            em.merge(affectation);
            tx.commit();

            logger.info("Clôture de l'affectation ID {} effectuée avec succès.", idAffectation);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la clôture de l'affectation", e);
            throw new RuntimeException("Erreur lors de la clôture de l'affectation", e);
        } finally {
            em.close();
        }
    }

    public List<Affectation> searchAffectation(String motCle) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = """
            SELECT a FROM Affectation a
            JOIN FETCH a.materiel m
            JOIN FETCH a.employe u
            WHERE LOWER(m.libelle) LIKE :mc
               OR LOWER(m.reference) LIKE :mc
               OR LOWER(u.nomComplet) LIKE :mc
               OR LOWER(u.email) LIKE :mc
               OR LOWER(a.commentaire) LIKE :mc
        """;

            return em.createQuery(jpql, Affectation.class)
                    .setParameter("mc", "%" + motCle.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
