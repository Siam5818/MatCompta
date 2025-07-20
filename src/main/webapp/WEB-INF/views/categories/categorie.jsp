<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Catégories - MatCompta</title>
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
                <i class="bi bi-tags me-2"></i> Catégories
            </h3>
            <p class="text-muted mb-0">Structurez vos matériels selon des groupes fonctionnels ou comptables</p>
        </div>

        <!-- Bouton ajout -->
        <div class="mb-3 text-end">
            <a href="categorie-servlet?action=add" class="btn btn-sm btn-primary">
                <i class="bi bi-plus-lg me-1"></i> Ajouter
            </a>
        </div>

        <!-- Barre de recherche -->
        <form method="get" action="categorie-servlet?action=search" class="mb-3">
            <div class="d-flex align-items-center" style="max-width: 500px;">
                <input type="text" name="query" class="form-control me-2" placeholder="Rechercher une catégorie..." value="${param.query}">
                <button type="submit" class="btn btn-outline-primary btn-sm px-2 py-1">
                    <i class="bi bi-search"></i>
                </button>
            </div>
        </form>

        <!-- Cartes de catégories -->
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
                    <c:forEach var="cat" items="${listeCategories}">
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h5 class="card-title text-primary">
                                        <i class="bi bi-tag-fill me-2"></i> ${cat.nom}
                                    </h5>
                                    <p class="card-text text-truncate" style="max-width: 100%;">
                                            ${cat.description}
                                    </p>
                                </div>
                                <div class="card-footer d-flex justify-content-between align-items-center">
                                    <span class="text-muted fst-italic">
                                        ${nombreMaterielsParCategorie[cat.id] != null ? nombreMaterielsParCategorie[cat.id] : 0} matériel(s)
                                    </span>
                                    <div class="d-flex gap-2">
                                        <a href="categorie-servlet?action=edit&id=${cat.id}" class="btn btn-sm btn-outline-primary" title="Modifier">
                                            <i class="bi bi-pencil-fill"></i>
                                        </a>
                                        <a href="categorie-servlet?action=delete&id=${cat.id}" class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Confirmer la suppression de la catégorie ${cat.nom} ?');"
                                           title="Supprimer">
                                            <i class="bi bi-trash-fill"></i>
                                        </a>
                                    </div>
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