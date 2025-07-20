<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sn.groupeisi.matcompta.entities.Utilisateur utilisateur = (sn.groupeisi.matcompta.entities.Utilisateur) request.getAttribute("utilisateur");
    boolean isEdit = utilisateur != null;
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Modifier" : "Ajouter" %> un utilisateur - MatCompta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Header -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <h3 class="mb-0">
                <i class="bi bi-person-plus me-2"></i>
                <%= isEdit ? "Modifier l'utilisateur" : "Ajouter un utilisateur" %>
            </h3>
            <p class="text-muted mb-0">Renseignez les informations personnelles et le rôle de l'utilisateur</p>
        </div>

        <!-- Formulaire -->
        <form method="post" action="utilisateur-servlet">
            <input type="hidden" name="id" value="<%= isEdit ? utilisateur.getId() : "" %>"/>
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>"/>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row g-3">

                        <!-- Nom complet -->
                        <div class="col-md-6">
                            <label for="nomComplet" class="form-label">Nom complet</label>
                            <input type="text" class="form-control" id="nomComplet" name="nomComplet"
                                   value="<%= isEdit ? utilisateur.getNomComplet() : "" %>" required>
                        </div>

                        <!-- Email -->
                        <div class="col-md-6">
                            <label for="email" class="form-label">Adresse email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<%= isEdit ? utilisateur.getEmail() : "" %>" required>
                        </div>

                        <!-- Téléphone -->
                        <div class="col-md-6">
                            <label for="phone" class="form-label">Téléphone</label>
                            <input type="text" class="form-control" id="phone" name="phone"
                                   value="<%= isEdit ? utilisateur.getPhone() : "" %>">
                        </div>

                        <!-- Mot de passe -->
                        <c:if test="${empty utilisateur.id}">
                            <div class="col-md-6">
                                <label for="motDePasse" class="form-label">Mot de passe</label>
                                <input type="password" class="form-control" id="motDePasse" name="motDePasse" required>
                            </div>
                        </c:if>

                        <!-- Rôle -->
                        <div class="col-md-6">
                            <label for="role" class="form-label">Rôle</label>
                            <select class="form-select" id="role" name="role" required>
                                <option value="" hidden>-- Choisissez le Rôle --</option>
                                <option value="EMPLOYEE" <%= isEdit && utilisateur.getRole().toString().equals("EMPLOYEE") ? "selected" : "" %>>Employé</option>
                                <option value="COMPTABLE" <%= isEdit && utilisateur.getRole().toString().equals("COMPTABLE") ? "selected" : "" %>>Comptable</option>
                                <option value="ADMIN" <%= isEdit && utilisateur.getRole().toString().equals("ADMIN") ? "selected" : "" %>>Administrateur</option>
                            </select>
                        </div>

                    </div>

                    <!-- Boutons -->
                    <div class="mt-4 d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-primary px-4">
                            <%= isEdit ? "Modifier" : "Enregistrer" %>
                        </button>
                        <a href="utilisateur-servlet" class="btn btn-outline-secondary">Annuler</a>
                    </div>

                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>