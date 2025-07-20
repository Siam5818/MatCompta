package sn.groupeisi.matcompta.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sn.groupeisi.matcompta.entities.Utilisateur;
import sn.groupeisi.matcompta.enums.RoleUser;
import sn.groupeisi.matcompta.requests.UtilisateurRequest;
import sn.groupeisi.matcompta.services.UtilisateurService;

import java.io.IOException;

@WebServlet(name = "registerServlet", value = "/register-servlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtilisateurRequest ur = new UtilisateurRequest();

        ur.setNomComplet(request.getParameter("nomcomplet"));
        ur.setEmail(request.getParameter("email"));
        ur.setPhone(request.getParameter("phone"));
        ur.setMotDePasse(request.getParameter("motDePasse"));
        ur.setRole(RoleUser.EMPLOYEE);

        try{
            UtilisateurService  service = new UtilisateurService();
            service.registerUtilisateur(ur);
            request.setAttribute("info", "Inscription réussie ! Connectez-vous.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erreur interne lors de l'inscription.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
}
