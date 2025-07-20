package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.entities.Affectation;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import sn.groupeisi.matcompta.requests.AffectationRequest;
import sn.groupeisi.matcompta.services.AffectationService;
import sn.groupeisi.matcompta.services.MaterialService;
import sn.groupeisi.matcompta.services.UtilisateurService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "AffectationServlet", value = "/affectation-servlet")
public class AffectationServlet extends HttpServlet {

    private AffectationService service;

    @Override
    public void init() {
        service = new AffectationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession(false) == null || req.getSession().getAttribute("utilisateur") == null) {
            resp.sendRedirect("login-servlet");
            return;
        }

        String action = req.getParameter("action");
        String motCle = req.getParameter("query");

        UtilisateurService us = new UtilisateurService();
        MaterialService ms = new MaterialService();

        if (action == null || action.equals("list") || action.equals("search")) {
            List<Affectation> liste = (motCle != null && !motCle.isBlank())
                    ? service.searchAffectation(motCle)
                    : service.listerToutesAffectations();

            req.setAttribute("listeAffectations", liste);
            req.setAttribute("query", motCle);
            req.getRequestDispatcher("/WEB-INF/views/affectations/affectation.jsp").forward(req, resp);

        } else if (action.equals("add")) {
            req.setAttribute("listeEmployes", us.listUtilisateur());
            req.setAttribute("listeMateriels", ms.listMaterielsDisponibles());
            req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));
            req.getRequestDispatcher("/WEB-INF/views/affectations/formAffectation.jsp").forward(req, resp);
        } else if (action.equals("signer")) {
            try {
                Long id = Long.parseLong(req.getParameter("id"));
                Affectation aff = service.findAffectationById(id);

                if (aff.getDateRetour() != null) {
                    req.getSession().setAttribute("errorMessage", "Cette affectation est déjà clôturée.");
                    resp.sendRedirect("affectation-servlet?action=list");
                    return;
                }

                req.setAttribute("affectation", aff);
                req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));
                req.setAttribute("listeEmployes", us.listUtilisateur());
                req.setAttribute("listeMateriels", ms.listMaterielsDisponibles());
                req.setAttribute("mode", "signer");

                req.getRequestDispatcher("/WEB-INF/views/affectations/formAffectation.jsp").forward(req, resp);

            } catch (Exception e) {
                req.getSession().setAttribute("errorMessage", "Impossible de charger l'affectation à signer.");
                resp.sendRedirect("affectation-servlet?action=list");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession(false) == null || req.getSession().getAttribute("utilisateur") == null) {
            resp.sendRedirect("login-servlet");
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            try {
                AffectationRequest ar = new AffectationRequest();
                ar.setEmployeId(Long.parseLong(req.getParameter("employeId")));
                ar.setMaterielId(Long.parseLong(req.getParameter("materielId")));
                ar.setDateAffectation(LocalDate.parse(req.getParameter("dateAffectation")));
                ar.setEtatMaterielAvant(EtatMateriel.valueOf(req.getParameter("etatAvant")));

                service.addAffectation(ar);

                req.getSession().setAttribute("successMessage", "Affectation enregistrée avec succès !");
                resp.sendRedirect("affectation-servlet?action=list");

            } catch (Exception e) {
                req.setAttribute("errorMessage", "Échec de l'enregistrement : " + e.getMessage());

                req.setAttribute("listeEmployes", new UtilisateurService().listUtilisateur());
                req.setAttribute("listeMateriels", new MaterialService().listMaterielsDisponibles());
                req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));

                req.getRequestDispatcher("/WEB-INF/views/affectations/formAffectation.jsp").forward(req, resp);
            }
        }

        if ("signer".equals(action)) {
            try {
                Long id = Long.parseLong(req.getParameter("id"));
                EtatMateriel etatApres = EtatMateriel.valueOf(req.getParameter("etatApres"));
                String commentaire = req.getParameter("commentaire");

                service.signerAffectation(id, etatApres, commentaire);

                req.getSession().setAttribute("successMessage", "Affectation clôturée avec succès !");
                resp.sendRedirect("affectation-servlet?action=list");

            } catch (Exception e) {
                req.setAttribute("errorMessage", "Échec de la clôture : " + e.getMessage());

                Affectation aff = service.findAffectationById(Long.parseLong(req.getParameter("id")));
                req.setAttribute("affectation", aff);
                req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));
                req.setAttribute("mode", "signer");

                req.getRequestDispatcher("/WEB-INF/views/affectations/formAffectation.jsp").forward(req, resp);
            }
        }
    }
}