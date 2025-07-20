<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<%@ include file="layout/header.jsp" %>

<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <title>Créer un compte</title>
</head>

<body class="bg-light d-flex align-items-center" style="height: 100vh;">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body p-4">
                    <h3 class="text-center mb-4">Créer un compte</h3>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty info}">
                        <div class="alert alert-success">${info}</div>
                    </c:if>
                    <%
                        session.removeAttribute("info");
                    %>

                    <form action="register-servlet" method="post">
                        <div class="mb-3">
                            <label for="nomcomplet" class="form-label">Nom complet :</label>
                            <input type="text" class="form-control" id="nomcomplet" name="nomcomplet" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email :</label>
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">Téléphone :</label>
                            <input type="tel" class="form-control" id="phone" name="phone"
                                   pattern="^[0-9]{9,15}$"
                                   title="Entrez un numéro valide (ex. +22177xxxxxxx ou 770000000)" required>
                        </div>

                        <div class="mb-3">
                            <label for="motDePasse" class="form-label">Mot de passe :</label>
                            <input type="password" class="form-control" id="motDePasse" name="motDePasse" required>
                        </div>

                        <button type="submit" class="btn btn-success w-100">Créer mon compte</button>
                    </form>

                    <div class="text-center mt-3">
                        <p class="mb-0">Déjà inscrit ?
                            <a href="login-servlet">Se connecter</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>