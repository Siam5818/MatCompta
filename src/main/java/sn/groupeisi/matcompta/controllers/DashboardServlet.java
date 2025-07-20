package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import sn.groupeisi.matcompta.services.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "DashboardServlet", value = "/dashboard-servlet")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔒 Vérification de session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            response.sendRedirect("login-servlet");
            return;
        }

        // 🔧 Initialisation des services
        MaterialService materielService = new MaterialService();
        UtilisateurService utilisateurService = new UtilisateurService();
        CategorieService categorieService = new CategorieService();
        AffectationService affectationService = new AffectationService();

        // 📦 Données globales
        request.setAttribute("totalMateriels", materielService.countTotalMateriel());
        request.setAttribute("totalUtilisateurs", utilisateurService.countTotalUtilisateurs());

        // 📊 Données sur les états
        List<EtatMateriel> etatMaterielList = List.of(EtatMateriel.values());
        Map<EtatMateriel, Integer> nombreParEtat = materielService.getNombreParEtat();
        request.setAttribute("etatMaterielList", etatMaterielList);
        request.setAttribute("nombreParEtat", nombreParEtat);

        // 🗂️ Données sur les catégories
        List<sn.groupeisi.matcompta.entities.Categorie> listeCategories = categorieService.listerCategorys();
        Map<String, Integer> nombreParCategorie = materielService.getNombreParCategorie(); // méthode à avoir côté service
        request.setAttribute("listeCategories", listeCategories);
        request.setAttribute("nombreParCategorie", nombreParCategorie);

        // 🧾 Liste des matériels récents
        request.setAttribute("listeMateriels", materielService.listerRecents());

        // 🧑‍🤝‍🧑 Affectations récentes
        request.setAttribute("affectationsRecentes", affectationService.listerAffectationsRecentes());

        // ⏩ Redirection vers vue
        request.getRequestDispatcher("WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}