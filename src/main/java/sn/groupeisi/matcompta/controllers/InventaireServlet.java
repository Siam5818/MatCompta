package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import sn.groupeisi.matcompta.entities.InventaireComptable;
import sn.groupeisi.matcompta.requests.InventaireRequest;
import sn.groupeisi.matcompta.services.InventaireService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "InventaireServlet", value = "/inventaire-servlet")
public class InventaireServlet extends HttpServlet {
    private InventaireService service;

    @Override
    public void init() throws ServletException {
        service = new InventaireService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if(action == null || action.equals("list")) {
            List<InventaireComptable> list = service.listInventaires();
            req.setAttribute("listeInventaires", list);
            req.getRequestDispatcher("/WEB-INF/views/inventaires/inventaire.jsp").forward(req, resp);
        } else if (action.equals("add")) {
            req.getRequestDispatcher("/WEB-INF/views/inventaires/formInventaire.jsp").forward(req, resp);
        }else if (action.equals("edit")) {
            Long id = Long.parseLong(req.getParameter("id"));
            InventaireComptable inv = service.findInventaireById(id);
            req.setAttribute("inventaire", inv);
            req.getRequestDispatcher("/WEB-INF/views/inventaires/formInventaire.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String libelle = req.getParameter("libelle");
        String departement = req.getParameter("departement");
        String dateStr = req.getParameter("dateInventaire");

        InventaireRequest ir = new InventaireRequest();
        ir.setLibelle(libelle);
        ir.setDepartement(departement);

        if (dateStr != null && !dateStr.isBlank()) {
            try {
                ir.setDateInventaire(LocalDate.parse(dateStr));
            } catch (DateTimeParseException e) {
                req.setAttribute("errorMessage", "Date invalide.");
                req.getRequestDispatcher("/WEB-INF/views/inventaires/formInventaire.jsp").forward(req, resp);
                return;
            }
        }

        try {


            if ("add".equals(action)) {
                service.addInventaire(ir);
                resp.sendRedirect("inventaire-servlet?action=list");
            } else if ("update".equals(action)) {
                Long id = Long.parseLong(req.getParameter("id"));
                service.modifyInventaire(id, ir);
                resp.sendRedirect("inventaire-servlet?action=list");
            }

        } catch (IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("inventaire", action.equals("update") ?
                    service.findInventaireById(Long.parseLong(req.getParameter("id"))) : null);
            req.getRequestDispatcher("/WEB-INF/views/inventaires/formInventaire.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Erreur technique lors du traitement.");
            req.getRequestDispatcher("/WEB-INF/views/inventaires/formInventaire.jsp").forward(req, resp);
        }
    }
}
