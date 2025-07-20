package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sn.groupeisi.matcompta.entities.Utilisateur;
import sn.groupeisi.matcompta.requests.UtilisateurRequest;
import sn.groupeisi.matcompta.services.UtilisateurService;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login-servlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("password");

        UtilisateurRequest ur = new UtilisateurRequest();
        ur.setEmail(email);
        ur.setMotDePasse(motDePasse);

        UtilisateurService us = new UtilisateurService();

        try{
            Utilisateur u = us.loginUtilisateur(email, motDePasse);
            HttpSession session = request.getSession();
            session.setAttribute("utilisateur", u);
            response.sendRedirect("dashboard-servlet");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Une erreur interne est survenue.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

}
