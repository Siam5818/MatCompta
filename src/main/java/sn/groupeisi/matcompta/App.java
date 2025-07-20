package sn.groupeisi.matcompta;

import java.io.*;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import sn.groupeisi.matcompta.services.UtilisateurService;
import sn.groupeisi.matcompta.utils.JpaUtil;

@WebServlet(name = "appServlet", value = "/app-servlet")
public class App extends HttpServlet {
    private String message;

    @Override
    public void init() throws ServletException {
        message = "Bienvenu dans MetaCompta!";
        EntityManager EManager = JpaUtil.getEntityManager();

        try(EManager){}

        new UtilisateurService().createDefaultAdmin();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void destroy() {
        JpaUtil.closeEntityManagerFactory();
    }
}