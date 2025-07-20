package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.entities.Utilisateur;
import sn.groupeisi.matcompta.enums.RoleUser;
import sn.groupeisi.matcompta.requests.UtilisateurRequest;
import sn.groupeisi.matcompta.services.UtilisateurService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UtilisateurServlet", value = "/utilisateur-servlet")
public class UtilisateurServlet extends HttpServlet {
    private UtilisateurService service;

    @Override
    public void init() throws ServletException {
        service = new UtilisateurService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null || action.equals("list") || action.equals("search")) {
            String motCle = req.getParameter("query");
            List<Utilisateur> liste = (motCle != null && !motCle.isBlank())
                    ? service.searchUtilisateursByMotCle(motCle)
                    : service.listUtilisateur();

            req.setAttribute("listeUtilisateurs", liste);
            req.setAttribute("query", motCle);
            req.getRequestDispatcher("/WEB-INF/views/utilisateurs/utilisateur.jsp").forward(req, resp);
        } else if (action.equals("add")) {
            req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Utilisateur u = service.findUtilisateurById(id);
            req.setAttribute("utilisateur", u);
            req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));

            Utilisateur user = service.findUtilisateurById(id);
            if (user.getRole().equals("ADMIN")) {
                req.setAttribute("errorMessage", "Suppression interdite pour un administrateur.");
                req.getRequestDispatcher("/WEB-INF/views/utilisateurs/utilisateur.jsp").forward(req, resp);
                return;
            }
            service.deleteUtilisateur(id);
            resp.sendRedirect("utilisateur-servlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Requête POST reçue ...");

        String action = req.getParameter("action");

        String nomComplet = req.getParameter("nomComplet");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String roleParam = req.getParameter("role");

        UtilisateurRequest utilisateur = new UtilisateurRequest();
        utilisateur.setNomComplet(nomComplet);
        utilisateur.setEmail(email);
        utilisateur.setPhone(phone);
        utilisateur.setRole(RoleUser.valueOf(roleParam));


        try {
            if ("add".equals(action)) {
                String motDePasse = "metacompta123";

                // Bloquer la création d'un ADMIN
                if ("ADMIN".equals(roleParam)) {
                    req.setAttribute("errorMessage", "La création d'un administrateur est interdite.");
                    req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
                    return;
                }

                utilisateur.setMotDePasse(motDePasse);

                if (!utilisateur.champValide()) {
                    req.setAttribute("errorMessage", "Les champs obligatoires sont invalides ou incomplets.");
                    req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
                    return;
                }

                service.registerUtilisateur(utilisateur);
                resp.sendRedirect("utilisateur-servlet?action=list");

            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                Utilisateur existing = service.findUtilisateurById(id);

                if (existing.getRole() == RoleUser.ADMIN && !existing.getRole().toString().equals(roleParam)) {
                    req.setAttribute("errorMessage", "Le rôle ADMIN ne peut pas être modifié.");
                    req.setAttribute("utilisateur", existing);
                    req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
                    return;
                }

                utilisateur.setMotDePasse(existing.getMotDePasse());

                if (!utilisateur.champValideSansMdp()) {
                    req.setAttribute("errorMessage", "Les champs obligatoires sont invalides ou incomplets.");
                    req.setAttribute("utilisateur", existing);
                    req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
                    return;
                }

                service.modifyUtilisateur(id, utilisateur);
                resp.sendRedirect("utilisateur-servlet?action=list");
            }
        } catch (IllegalArgumentException e) {
            // Capture les erreurs de validation levées dans le service
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("utilisateur", utilisateur);
            req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
        } catch (Exception e) {
            // Erreurs inattendues
            req.setAttribute("errorMessage", "Une erreur est survenue. Veuillez réessayer plus tard.");
            req.getRequestDispatcher("/WEB-INF/views/utilisateurs/formUtilisateur.jsp").forward(req, resp);
        }
    }
}
