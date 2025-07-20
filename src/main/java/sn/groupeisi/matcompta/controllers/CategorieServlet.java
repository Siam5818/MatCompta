package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.entities.Categorie;
import sn.groupeisi.matcompta.requests.CategorieRequest;
import sn.groupeisi.matcompta.services.CategorieService;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "CategorieServlet", value = "/categorie-servlet")
public class CategorieServlet extends HttpServlet {
    private CategorieService service;

    @Override
    public void init() throws ServletException {
        service = new CategorieService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null || action.equals("list") || action.equals("search")) {
            String motCle = req.getParameter("query");

            List<Categorie> liste = (motCle != null && !motCle.isBlank())
                    ? service.searchCategories(motCle)
                    : service.listerCategorys();

            Map<Long, Integer> nombreMaterielsParCategorie = new HashMap<>();
            for (Categorie cat : liste) {
                int count = service.countMaterielsByCategorie(cat.getId());
                nombreMaterielsParCategorie.put(cat.getId(), count);
            }

            req.setAttribute("listeCategories", liste);
            req.setAttribute("nombreMaterielsParCategorie", nombreMaterielsParCategorie);
            req.setAttribute("query", motCle); // pour garder le champ rempli
            req.getRequestDispatcher("/WEB-INF/views/categories/categorie.jsp").forward(req, resp);
        } else if (action.equals("add")) {
            req.getRequestDispatcher("/WEB-INF/views/categories/formCategorie.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Categorie cat = service.findCategoryById(id);
            req.setAttribute("categorie", cat);
            req.getRequestDispatcher("/WEB-INF/views/categories/formCategorie.jsp").forward(req, resp);

        } else if (action.equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
            service.deleteCategory(id);
            resp.sendRedirect("categorie-servlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String nom = req.getParameter("nom");
        String description = req.getParameter("description");

        CategorieRequest cr = new CategorieRequest();
        cr.setNom(nom);
        cr.setDescription(description);

        try {
            if ("add".equals(action)) {
                service.addCategory(cr);
                resp.sendRedirect("categorie-servlet?action=list");
            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.modifyCategory(id, cr);
                resp.sendRedirect("categorie-servlet?action=list");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());

            if ("update".equals(action)) {
                req.setAttribute("categorie", service.findCategoryById(Long.parseLong(req.getParameter("id"))));
            }

            req.getRequestDispatcher("/WEB-INF/views/categories/formCategorie.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Erreur technique lors du traitement.");
            req.getRequestDispatcher("/WEB-INF/views/categories/formCategorie.jsp").forward(req, resp);
        }
    }

}
