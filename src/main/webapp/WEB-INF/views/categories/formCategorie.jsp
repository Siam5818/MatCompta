<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sn.groupeisi.matcompta.entities.Categorie categorie = (sn.groupeisi.matcompta.entities.Categorie) request.getAttribute("categorie");
    boolean isEdit = categorie != null;
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Modifier" : "Ajouter" %> une catégorie - MatCompta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Header designé -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <h3 class="mb-0">
                <i class="bi bi-tags me-2"></i>
                <%= isEdit ? "Modifier la catégorie" : "Ajouter une catégorie" %>
            </h3>
            <p class="text-muted mb-0">Définissez le libellé et la description utile pour les matériels</p>
        </div>

        <!-- Formulaire -->
        <form method="post" action="categorie-servlet">
            <input type="hidden" name="id" value="<%= isEdit ? categorie.getId() : "" %>"/>
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>"/>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row g-3">

                        <!-- Nom -->
                        <div class="col-md-6">
                            <label for="nom" class="form-label">Nom de la catégorie</label>
                            <input type="text" class="form-control" id="nom" name="nom"
                                   value="<%= isEdit ? categorie.getNom() : "" %>" required>
                        </div>

                        <!-- Description -->
                        <div class="col-md-12">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"
                                      placeholder="Décrivez l’usage ou le regroupement visé"><%= isEdit && categorie.getDescription() != null ? categorie.getDescription() : "" %></textarea>
                        </div>

                    </div>

                    <!-- Boutons -->
                    <div class="mt-4 d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-primary px-4">
                            <%= isEdit ? "Modifier" : "Enregistrer" %>
                        </button>
                        <a href="categorie-servlet?action=list" class="btn btn-outline-secondary">Annuler</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>