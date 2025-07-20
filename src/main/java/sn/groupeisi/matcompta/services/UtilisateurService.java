package sn.groupeisi.matcompta.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import org.slf4j.*;
import sn.groupeisi.matcompta.entities.Utilisateur;
import sn.groupeisi.matcompta.enums.RoleUser;
import sn.groupeisi.matcompta.requests.UtilisateurRequest;
import sn.groupeisi.matcompta.utils.JpaUtil;
import sn.groupeisi.matcompta.utils.PasswordUtil;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurService {
    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    public void registerUtilisateur(UtilisateurRequest ur) {
        if (!ur.champValide()) {
            logger.warn("Champs invalides ou incomplets pour : {}", ur.getEmail());
            throw new IllegalArgumentException("Les champs obligatoires sont invalides ou incomplets.");
        }

        if (!ur.estEmailValide()) {
            logger.warn("Email invalide : {}", ur.getEmail());
            throw new IllegalArgumentException("Le format de l'adresse email est invalide.");
        }

        if (emailExiste(ur.getEmail())) {
            logger.warn("Tentative d'enregistrement avec un email déjà utilisé : {}", ur.getEmail());
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        String mdpHash = PasswordUtil.hasher(ur.getMotDePasse());
        Utilisateur utilisateur = Utilisateur.builder()
                .nomComplet(ur.getNomComplet())
                .email(ur.getEmail())
                .phone(ur.getPhone())
                .motDePasse(mdpHash)
                .role(ur.getRole()).build();

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(utilisateur);
            transaction.commit();
            logger.info("Utilisateur enregistré avec succès : {}", utilisateur.getEmail());
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            logger.error("Erreur lors de l'enregistrement de l'utilisateur : {}", utilisateur.getEmail(), e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur", e);
        } finally {
            em.close();
        }
    }

    public Utilisateur loginUtilisateur(String email, String motDePasse) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            Utilisateur utilisateur = em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (PasswordUtil.verifier(motDePasse, utilisateur.getMotDePasse())) {
                logger.info("Connexion réussie pour : {}", email);
                return utilisateur;
            } else {
                logger.warn("Échec de connexion : mot de passe incorrect pour {}", email);
                throw new IllegalArgumentException("Mot de passe incorrect.");
            }
        } catch (NoResultException e) {
            logger.warn("Échec de connexion : utilisateur introuvable pour {}", email);
            throw new IllegalArgumentException("Email non reconnu.");
        } catch (Exception e) {
            logger.error("Erreur lors de la tentative de connexion pour {}", email, e);
            throw new RuntimeException("Erreur interne lors de la connexion.", e);
        } finally {
            em.close();
        }
    }

    public void modifyUtilisateur(Long id, UtilisateurRequest ur) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Utilisateur utilisateur = em.find(Utilisateur.class, id);
            if (utilisateur == null) {
                logger.warn("Modification non aboutie : utilisateur introuvable avec ID {}", id);
                throw new IllegalArgumentException("Utilisateur introuvable.");
            }

            if (!utilisateur.getEmail().equals(ur.getEmail()) && emailExiste(ur.getEmail())) {
                logger.warn("L'email {} est déjà utilisé par un autre utilisateur", ur.getEmail());
                throw new IllegalArgumentException("Cet email est déjà utilisé.");
            }

            tx.begin();
            utilisateur.setNomComplet(ur.getNomComplet());
            utilisateur.setPhone(ur.getPhone());
            utilisateur.setEmail(ur.getEmail());

            if (ur.getMotDePasse() != null && !ur.getMotDePasse().isBlank()) {
                utilisateur.setMotDePasse(PasswordUtil.hasher(ur.getMotDePasse()));
            }

            // Modifier le rôle sauf si utilisateur est ADMIN (ne pas modifier le rôle admin)
            if (utilisateur.getRole() != RoleUser.ADMIN) {
                utilisateur.setRole(ur.getRole());
            } else {
                logger.info("Rôle ADMIN ne peut pas être modifié pour l'utilisateur ID {}", id);
            }

            tx.commit();
            logger.info("Utilisateur modifié avec succès : {}", utilisateur.getEmail());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la modification de l'utilisateur {}", id, e);
            throw new RuntimeException("Erreur lors de la modification de l'utilisateur", e);
        } finally {
            em.close();
        }
    }

    public void deleteUtilisateur(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Utilisateur utilisateur = em.find(Utilisateur.class, id);
            if (utilisateur == null) {
                logger.warn("Suppression échouée : utilisateur introuvable avec ID {}", id);
                throw new IllegalArgumentException("Utilisateur introuvable.");
            }

            tx.begin();
            em.remove(utilisateur);
            tx.commit();
            logger.info("Utilisateur supprimé avec succès : {}", utilisateur.getEmail());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Erreur lors de la suppression de l'utilisateur {}", id, e);
            throw new RuntimeException("Erreur lors de la suppression", e);
        } finally {
            em.close();
        }
    }

    public Utilisateur findUtilisateurById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Utilisateur utilisateur = em.find(Utilisateur.class, id);
            if (utilisateur == null) {
                logger.warn("Aucun utilisateur trouvé avec l'ID {}", id);
                throw new IllegalArgumentException("Utilisateur introuvable.");
            }
            return utilisateur;
        } finally {
            em.close();
        }
    }

    public Utilisateur findUtilisateurByEmail(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            logger.warn("Aucun utilisateur trouvé avec l'email {}", email);
            throw new IllegalArgumentException("Utilisateur introuvable.");
        } finally {
            em.close();
        }
    }

    public List<Utilisateur> listUtilisateur() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Utilisateur> searchUtilisateursByMotCle(String motCle) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            String jpql = "SELECT u FROM Utilisateur u " +
                    "WHERE LOWER(u.nomComplet) LIKE :motCle " +
                    "OR LOWER(u.email) LIKE :motCle";

            return em.createQuery(jpql, Utilisateur.class)
                    .setParameter("motCle", "%" + motCle.toLowerCase() + "%")
                    .getResultList();

        } catch (Exception e) {
            logger.error("Erreur lors de la recherche par mot-clé : {}", motCle, e);
            throw new RuntimeException("Erreur lors de la recherche", e);
        } finally {
            em.close();
        }
    }

    public long countUtilisateursByRole(RoleUser role) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u WHERE u.role = :role", Long.class)
                    .setParameter("role", role)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public long countTotalUtilisateurs() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM Utilisateur u", Long.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public boolean emailExiste(String email) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'email : {}", email, e);
            throw e;
        } finally {
            em.close();
        }
    }

    public void createDefaultAdmin() {
        String adminEmail = "superadmin@metacompta.sn";
        if (!emailExiste(adminEmail)) {
            String hash = PasswordUtil.hasher("metacompta123"); // mot de passe par défaut

            Utilisateur admin = Utilisateur.builder()
                    .nomComplet("Super Admin")
                    .email(adminEmail)
                    .motDePasse(hash)
                    .phone("123456789")
                    .role(RoleUser.ADMIN)
                    .build();

            EntityManager em = JpaUtil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(admin);
                tx.commit();
                logger.info("Admin par défaut créé avec succès : {}", adminEmail);
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                logger.error("Erreur lors de la création de l'admin par défaut", e);
                throw new RuntimeException("Erreur lors de la création de l'admin", e);
            } finally {
                em.close();
            }
        } else {
            logger.info("Admin par défaut déjà existant : {}", adminEmail);
        }
    }
}
