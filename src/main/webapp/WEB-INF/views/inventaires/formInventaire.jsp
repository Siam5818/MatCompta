<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sn.groupeisi.matcompta.entities.InventaireComptable inventaire = (sn.groupeisi.matcompta.entities.InventaireComptable) request.getAttribute("inventaire");
    boolean isEdit = inventaire != null;

    String dateValue = "";
    if (isEdit && inventaire.getDateInventaire() != null) {
        dateValue = inventaire.getDateInventaire().toString();
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Modifier" : "Ajouter" %> un inventaire - MatCompta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <c:if test="${not empty errorMessage}">
        <div class="container mt-3">
            <div class="alert alert-danger">${errorMessage}</div>
        </div>
    </c:if>

    <div class="container mt-4">

        <!-- Header designé -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <h3 class="mb-0">
                <i class="bi bi-journal-text me-2"></i>
                <%= isEdit ? "Modifier l'inventaire" : "Ajouter un inventaire" %>
            </h3>
            <p class="text-muted mb-0">Renseignez les informations comptables liées aux biens matériels</p>
        </div>

        <!-- Formulaire -->
        <form method="post" action="inventaire-servlet">
            <input type="hidden" name="id" value="<%= isEdit ? inventaire.getId() : "" %>"/>
            <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>"/>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row g-3">
                        <!-- Libellé -->
                        <div class="col-md-6">
                            <label for="libelle" class="form-label">Libellé</label>
                            <input type="text" class="form-control" id="libelle" name="libelle"
                                   value="<%= isEdit ? inventaire.getLibelle() : "" %>" required>
                        </div>

                        <!-- Département -->
                        <div class="col-md-6">
                            <label for="departement" class="form-label">Département</label>
                            <input type="text" class="form-control" id="departement" name="departement"
                                   value="<%= isEdit ? inventaire.getDepartement() : "" %>">
                        </div>

                        <!-- Date d'inventaire -->
                        <div class="col-md-6">
                            <label for="dateInventaire" class="form-label">Date d'inventaire</label>
                            <input type="date" class="form-control" id="dateInventaire" name="dateInventaire"
                                   value="<%= dateValue %>" required>
                        </div>
                    </div>

                    <!-- Boutons -->
                    <div class="mt-4 d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-primary px-4">
                            <%= isEdit ? "Modifier" : "Enregistrer" %>
                        </button>
                        <a href="inventaire-servlet?action=list" class="btn btn-outline-secondary">Annuler</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>