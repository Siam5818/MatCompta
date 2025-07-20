<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Matériels - MatCompta</title>

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Feedback -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Header designé -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <h3 class="mb-0">
                <i class="bi bi-box-seam me-2"></i> Matériels
            </h3>
            <p class="text-muted mb-0">Inventaire des matériels enregistrés et leur statut</p>
        </div>

        <!-- Bouton ajout -->
        <div class="mb-3 text-end">
            <a href="materiel-servlet?action=add" class="btn btn-sm btn-primary">
                <i class="bi bi-plus-lg me-1"></i> Ajouter
            </a>
        </div>

        <!-- Barre de recherche -->
        <form method="get" action="materiel-servlet?action=search" class="mb-3">
            <div class="d-flex align-items-center" style="max-width: 500px;">
                <input type="text" name="query" class="form-control me-2" placeholder="Rechercher un matériel..." value="${param.query}">
                <button type="submit" class="btn btn-outline-primary btn-sm px-2 py-1">
                    <i class="bi bi-search"></i>
                </button>
            </div>
        </form>

        <!-- Cartes des matériels -->
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
                    <c:forEach var="m" items="${listeMateriels}">
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h5 class="card-title text-primary">
                                        <i class="bi bi-cpu me-2"></i> ${m.libelle}
                                    </h5>
                                    <p class="mb-1"><strong>Catégorie :</strong> ${m.categorie.nom}</p>
                                    <p class="mb-1">
                                        <strong>État :</strong>
                                        <span class="badge
                                            <c:choose>
                                                <c:when test="${m.etat == 'EN_SERVICE'}">bg-success</c:when>
                                                <c:when test="${m.etat == 'A_REPARER'}">bg-warning</c:when>
                                                <c:when test="${m.etat == 'HORS_SERVICE'}">bg-danger</c:when>
                                                <c:otherwise>bg-secondary</c:otherwise>
                                            </c:choose>">${m.etat}</span>
                                    </p>
                                    <p class="mb-1"><strong>Valeur :</strong> ${m.valeurAchat} FCFA</p>
                                    <p class="mb-1"><strong>Date Achat :</strong> ${m.dateAchat} FCFA</p>
                                </div>
                                <div class="card-footer d-flex justify-content-end gap-2">
                                    <a href="materiel-servlet?action=edit&id=${m.id}" class="btn btn-sm btn-outline-primary" title="Modifier">
                                        <i class="bi bi-pencil-fill"></i>
                                    </a>
                                    <a href="materiel-servlet?action=delete&id=${m.id}" class="btn btn-sm btn-outline-danger"
                                       onclick="return confirm('Confirmer la suppression du matériel ${m.libelle} ?');" title="Supprimer">
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