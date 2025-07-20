package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.entities.*;
import sn.groupeisi.matcompta.enums.EtatMateriel;
import sn.groupeisi.matcompta.requests.MaterielRequest;
import sn.groupeisi.matcompta.services.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MaterielServlet", value = "/materiel-servlet")
public class MaterielServlet extends HttpServlet {
    private MaterialService service;

    @Override
    public void init() throws ServletException {
        service = new MaterialService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        MaterielRequest mr = new MaterielRequest();
        mr.setLibelle(req.getParameter("libelle"));
        mr.setReference(req.getParameter("reference"));
        mr.setDateAchat(LocalDate.parse(req.getParameter("dateAchat")));
        mr.setValeurAchat(Double.parseDouble(req.getParameter("valeurAchat")));
        mr.setDureeAmortissement(Integer.parseInt(req.getParameter("dureeAmortissement")));
        mr.setEmplacement(req.getParameter("emplacement"));
        mr.setEtat(EtatMateriel.valueOf(req.getParameter("etat")));
        mr.setCategorieId(Long.parseLong(req.getParameter("categorieId")));

        String inventaireIdStr = req.getParameter("inventaireId");
        if (inventaireIdStr != null && !inventaireIdStr.isBlank()) {
            mr.setInventaireId(Long.parseLong(inventaireIdStr));
        }

        try {
            if ("add".equals(action)) {
                service.addMateriel(mr);
            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.modifyMateriel(id, mr);
            }
            resp.sendRedirect("materiel-servlet?action=list");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp); // Réaffiche le formulaire avec les messages d’erreur
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        CategorieService c = new CategorieService();
        InventaireService ic = new InventaireService();

        if (action == null || action.equals("list") || action.equals("search")) {
            String motCle = req.getParameter("query");
            List<Materiel> liste = (motCle != null && !motCle.isBlank())
                    ? service.searchMateriel(motCle)
                    : service.listerMateriels();

            req.setAttribute("listeMateriels", liste);
            req.setAttribute("query", motCle);
            req.getRequestDispatcher("/WEB-INF/views/materiels/materiel.jsp").forward(req, resp);

        } else if (action.equals("add")) {
            req.setAttribute("etatMaterielList", EtatMateriel.values());
            req.setAttribute("listeCategories", c.listerCategorys());
            req.setAttribute("listeInventaires", ic.listInventaires());
            req.getRequestDispatcher("/WEB-INF/views/materiels/formMateriel.jsp").forward(req, resp);
        }else if (action.equals("edit")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Materiel m = service.findMaterielById(id);
            req.setAttribute("materiel", m);

            req.setAttribute("listeInventaires", ic.listInventaires());
            req.setAttribute("listeCategories", c.listerCategorys());
            req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));

            req.getRequestDispatcher("/WEB-INF/views/materiels/formMateriel.jsp").forward(req, resp);
        }else if (action.equals("delete")) {
            Long id = Long.parseLong(req.getParameter("id"));
        }else if ("etatSuivi".equals(action)) {
            List<Materiel> liste = service.listerMateriels();
            req.setAttribute("listeMateriels", liste);

            req.setAttribute("etatMaterielList", List.of(EtatMateriel.values()));

            Map<EtatMateriel, Integer> nombreParEtat = new EnumMap<>(EtatMateriel.class);
            for (EtatMateriel etat : EtatMateriel.values()) {
                int count = service.countMaterielByEtat(etat);
                nombreParEtat.put(etat, count);
            }
            req.setAttribute("nombreParEtat", nombreParEtat);

            req.getRequestDispatcher("/WEB-INF/views/materiels/etatSuivi.jsp").forward(req, resp);
        }
    }
}
