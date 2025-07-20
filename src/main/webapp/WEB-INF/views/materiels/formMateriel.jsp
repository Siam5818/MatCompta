<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${materiel != null ? "Modifier" : "Ajouter"} un matériel - MatCompta</title>
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
                <i class="bi bi-box-seam me-2"></i>
                ${materiel != null ? "Modifier le matériel" : "Ajouter un matériel"}
            </h3>
            <p class="text-muted mb-0">Renseignez les informations techniques et comptables du matériel</p>
        </div>

        <!-- Formulaire -->
        <form method="post" action="materiel-servlet">
            <input type="hidden" name="id" value="${materiel.id}"/>
            <input type="hidden" name="action" value="${materiel != null ? 'update' : 'add'}"/>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row g-3">
                        <!-- Libellé -->
                        <div class="col-md-6">
                            <label for="libelle" class="form-label">Libellé</label>
                            <input type="text" class="form-control" id="libelle" name="libelle" value="${materiel.libelle}" required>
                        </div>

                        <!-- Référence -->
                        <div class="col-md-6">
                            <label for="reference" class="form-label">Référence</label>
                            <input type="text" class="form-control" id="reference" name="reference" value="${materiel.reference}" required>
                        </div>

                        <!-- Date d'achat -->
                        <div class="col-md-6">
                            <label for="dateAchat" class="form-label">Date d'achat</label>
                            <input type="date" class="form-control" id="dateAchat" name="dateAchat" value="${materiel.dateAchat}" required>
                        </div>

                        <!-- Valeur d'achat -->
                        <div class="col-md-6">
                            <label for="valeurAchat" class="form-label">Valeur d'achat (FCFA)</label>
                            <input type="number" class="form-control" id="valeurAchat" name="valeurAchat" step="0.01" min="0" value="${materiel.valeurAchat}" required>
                        </div>

                        <!-- Durée amortissement -->
                        <div class="col-md-6">
                            <label for="dureeAmortissement" class="form-label">Durée d’amortissement (mois)</label>
                            <input type="number" class="form-control" id="dureeAmortissement" name="dureeAmortissement" min="1" value="${materiel.dureeAmortissement}" required>
                        </div>

                        <!-- Emplacement -->
                        <div class="col-md-6">
                            <label for="emplacement" class="form-label">Emplacement</label>
                            <input type="text" class="form-control" id="emplacement" name="emplacement" value="${materiel.emplacement}">
                        </div>

                        <!-- État -->
                        <div class="col-md-6">
                            <label for="etat" class="form-label">État</label>
                            <select class="form-select" id="etat" name="etat" required>
                                <option value="" disabled selected>-- Choisissez l'état --</option>
                                <c:forEach var="e" items="${etatMaterielList}">
                                    <option value="${e}" ${materiel.etat == e ? 'selected' : ''}>${e}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Catégorie -->
                        <div class="col-md-6">
                            <label for="categorieId" class="form-label">Catégorie</label>
                            <select class="form-select" id="categorieId" name="categorieId" required>
                                <option value="" disabled selected>-- Choisissez une catégorie --</option>
                                <c:forEach var="cat" items="${listeCategories}">
                                    <option value="${cat.id}" ${materiel.categorie.id == cat.id ? 'selected' : ''}>${cat.nom}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Inventaire -->
                        <div class="col-md-6">
                            <label for="inventaireId" class="form-label">Inventaire Comptable</label>
                            <select class="form-select" id="inventaireId" name="inventaireId">
                                <option value="" disabled selected>-- Choisissez une inventaire --</option>
                                <c:forEach var="inv" items="${listeInventaires}">
                                    <c:set var="selected" value="${materiel != null && materiel.inventaireComptable != null && materiel.inventaireComptable.id == inv.id}" />
                                    <option value="${inv.id}" ${selected ? 'selected' : ''}>${inv.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <!-- Boutons -->
                    <div class="mt-4 d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-primary px-4">
                            ${materiel != null ? "Modifier" : "Enregistrer"}
                        </button>
                        <a href="materiel-servlet?action=list" class="btn btn-outline-secondary">Annuler</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>