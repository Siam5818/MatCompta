<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${mode == 'signer' ? 'Clôturer' : 'Nouvelle'} Affectation - MatCompta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Header -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <h3 class="mb-0">
                <i class="bi bi-arrow-left-right me-2"></i>
                ${mode == 'signer' ? "Clôturer une affectation" : "Nouvelle affectation"}
            </h3>
            <p class="text-muted mb-0">
                <c:choose>
                    <c:when test="${mode == 'signer'}">Finalisez le retour du matériel par l'employé</c:when>
                    <c:otherwise>Attribuez un matériel à un employé</c:otherwise>
                </c:choose>
            </p>
        </div>

        <!-- Formulaire -->
        <form method="post" action="affectation-servlet">
            <input type="hidden" name="action" value="${mode == 'signer' ? 'signer' : 'add'}"/>
            <c:if test="${affectation != null}">
                <input type="hidden" name="id" value="${affectation.id}"/>
            </c:if>

            <div class="card shadow-sm">
                <div class="card-body">
                    <div class="row g-3">

                        <!-- Employé -->
                        <div class="col-md-6">
                            <label class="form-label">Employé</label>
                            <select class="form-select" name="employeId" required ${mode == 'signer' ? 'disabled' : ''}>
                                <option value="" hidden>Choisir...</option>
                                <c:forEach var="u" items="${listeEmployes}">
                                    <option value="${u.id}" ${affectation != null && affectation.employe.id == u.id ? 'selected' : ''}>${u.nomComplet}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Matériel -->
                        <div class="col-md-6">
                            <label class="form-label">Matériel</label>
                            <select class="form-select" name="materielId"
                                    required ${mode == 'signer' ? 'disabled' : ''}>
                                <option value="" hidden>Choisir...</option>
                                <c:forEach var="m" items="${listeMateriels}">
                                    <option value="${m.id}" ${affectation != null && affectation.materiel.id == m.id ? 'selected' : ''}>
                                            ${m.libelle} - ${m.reference}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Date Affectation -->
                        <div class="col-md-6">
                            <label class="form-label">Date d'affectation</label>
                            <input type="date" class="form-control" name="dateAffectation"
                                   value="${affectation != null ? affectation.dateAffectation : ''}"
                            ${mode == 'signer' ? 'disabled' : ''} required>
                        </div>

                        <!-- État Avant -->
                        <div class="col-md-6">
                            <label class="form-label">État avant affectation</label>
                            <select class="form-select" name="etatAvant" required ${mode == 'signer' ? 'disabled' : ''}>
                                <option value="" hidden>Choisir...</option>
                                <c:forEach var="e" items="${etatMaterielList}">
                                    <option value="${e}" ${affectation != null && affectation.etatMaterielAvant == e ? 'selected' : ''}>${e}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Date Retour -->
                        <div class="col-md-6">
                            <label class="form-label">Date de retour</label>
                            <input type="date" class="form-control" name="dateRetour"
                                   value="${affectation != null && affectation.dateRetour != null ? affectation.dateRetour : ''}"
                            ${mode == 'signer' ? '' : 'disabled'}>
                        </div>

                        <!-- État Après -->
                        <div class="col-md-6">
                            <label class="form-label">État après affectation</label>
                            <select class="form-select" name="etatApres" ${mode == 'signer' ? '' : 'disabled'}>
                                <option value="" hidden>Non signé</option>
                                <c:forEach var="e" items="${etatMaterielList}">
                                    <option value="${e}" ${affectation != null && affectation.etatMaterielApres == e ? 'selected' : ''}>${e}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Commentaire -->
                        <div class="col-12">
                            <label class="form-label">Commentaire</label>
                            <textarea class="form-control" name="commentaire" rows="3"
                            ${mode == 'signer' ? '' : 'disabled'}
                                      placeholder="Observations ou précisions...">${affectation != null ? affectation.commentaire : ''}</textarea>
                        </div>
                    </div>

                    <!-- Boutons -->
                    <div class="mt-4 d-flex justify-content-end gap-2">
                        <button type="submit" class="btn btn-${mode == 'signer' ? 'success' : 'primary'} px-4">
                            ${mode == 'signer' ? 'Clôturer' : 'Enregistrer'}
                        </button>
                        <a href="affectation-servlet?action=list" class="btn btn-outline-secondary">
                            Annuler
                        </a>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>