<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inventaires Comptables - MatCompta</title>
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
                <i class="bi bi-journal-text me-2"></i> Inventaires comptables
            </h3>
            <p class="text-muted mb-0">Visualisez les inventaires liés aux matériels enregistrés</p>
        </div>

        <!-- Bouton ajout -->
        <div class="mb-3 text-end">
            <a href="inventaire-servlet?action=add" class="btn btn-sm btn-primary">
                <i class="bi bi-plus-lg me-1"></i> Ajouter
            </a>
        </div>

        <!-- Cartes d'inventaire -->
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
                    <c:forEach var="inv" items="${listeInventaires}">
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h5 class="card-title text-primary">
                                        <i class="bi bi-journal me-2"></i> ${inv.libelle}
                                    </h5>
                                    <p class="mb-1"><strong>Département :</strong> ${inv.departement}</p>
                                    <p class="mb-1"><strong>Date :</strong> ${inv.dateInventaire}</p>
                                    <p class="mb-1 text-muted fst-italic">
                                            ${inv.materiels != null ? inv.materiels.size() : 0} matériel(s)
                                    </p>
                                </div>
                                <div class="card-footer d-flex justify-content-end gap-2">
                                    <a href="inventaire-servlet?action=edit&id=${inv.id}" class="btn btn-sm btn-outline-primary" title="Modifier">
                                        <i class="bi bi-pencil-fill"></i>
                                    </a>
                                    <a href="inventaire-servlet?action=delete&id=${inv.id}" class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Confirmer la suppression de l\'inventaire ${inv.libelle} ?');"
                                       title="Supprimer">
                                        <i class="bi bi-trash-fill"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>