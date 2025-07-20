<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<div class="sidebar bg-dark text-white"
     style="width:220px; height:100vh; position:fixed; top:0; left:0; padding-top:20px;">
    <h4 class="text-center">MatCompta</h4>
    <hr class="mx-3 text-white">

    <!-- Dashboard -->
    <a href="dashboard-servlet" class="d-block px-3 py-2 text-white text-decoration-none">
        <i class="bi bi-speedometer2 me-2"></i> Dashboard
    </a>

    <!-- Utilisateurs -->
    <a href="utilisateur-servlet" class="d-block px-3 py-2 text-white text-decoration-none">
        <i class="bi bi-people me-2"></i> Utilisateurs
    </a>

    <!-- Matériels -->
    <button class="btn btn-sm text-start w-100 text-white px-3 py-2 border-0 bg-transparent" data-bs-toggle="collapse"
            data-bs-target="#materielMenu">
        <i class="bi bi-box-seam me-2"></i> Matériels <i class="bi bi-chevron-down float-end"></i>
    </button>
    <div class="collapse" id="materielMenu">
        <a href="materiel-servlet" class="d-block px-4 py-1 text-white text-decoration-none">Liste complète</a>
        <a href="materiel-servlet?action=etatSuivi" class="d-block px-4 py-1 text-white text-decoration-none">État & suivi</a>
    </div>

    <!-- Affectations -->
    <a href="affectation-servlet" class="d-block px-4 py-1 text-white text-decoration-none">
        <i class="bi bi-arrow-left-right me-2"></i>Affectations
    </a>

    <!-- Catégories -->
    <a href="categorie-servlet" class="d-block px-3 py-2 text-white text-decoration-none">
        <i class="bi bi-tags me-2"></i> Catégories
    </a>

    <!-- Inventaires -->
    <a href="inventaire-servlet" class="d-block px-3 py-2 text-white text-decoration-none">
        <i class="bi bi-clipboard-data me-2"></i> Inventaires
    </a>

    <!-- Déconnexion -->
    <a href="logout-servlet" class="d-block px-3 py-2 text-white text-decoration-none">
        <i class="bi bi-box-arrow-right me-2"></i> Déconnexion
    </a>
</div>
