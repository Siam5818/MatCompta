<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Affectations - MatCompta</title>

    <!-- Styles -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css"/>

    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Titre et bouton -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="fw-bold text-primary">
                <i class="bi bi-arrow-left-right me-2"></i> Affectations
            </h3>
            <a href="affectation-servlet?action=add" class="btn btn-sm btn-primary">
                <i class="bi bi-plus-lg me-1"></i> Ajouter
            </a>
        </div>

        <!-- Tableau -->
        <div class="card shadow-sm">
            <div class="card-body table-responsive">
                <table class="table table-bordered table-hover align-middle" id="affectationTable">
                    <thead class="table-light">
                    <tr>
                        <th>Matériel</th>
                        <th>Employé</th>
                        <th>Date d'affectation</th>
                        <th>Retour</th>
                        <th>État après</th>
                        <th>Commentaire</th>
                        <th class="text-end">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="a" items="${listeAffectations}">
                        <tr>
                            <td>${a.materiel.libelle}</td>
                            <td>${a.employe.nomComplet}</td>
                            <td>
                                    ${a.dateAffectation.dayOfMonth}/${a.dateAffectation.monthValue}/${a.dateAffectation.year}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${a.dateRetour != null}">
                                        ${a.dateRetour.dayOfMonth}/${a.dateRetour.monthValue}/${a.dateRetour.year}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">En cours</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty a.etatMaterielApres}">
                                            <span class="badge
                                                ${a.etatMaterielApres == 'EN_SERVICE' ? 'bg-success' :
                                                  a.etatMaterielApres == 'A_REPARER' ? 'bg-warning text-dark' :
                                                  a.etatMaterielApres == 'HORS_SERVICE' ? 'bg-danger' :
                                                  'bg-secondary'}">
                                                    ${a.etatMaterielApres}
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted fst-italic">Non signé</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${a.commentaire != null ? a.commentaire : "—"}</td>
                            <td class="text-end">
                                <c:choose>
                                    <c:when test="${a.dateRetour == null}">
                                        <a href="affectation-servlet?action=signer&id=${a.id}"
                                           class="btn btn-sm btn-outline-success" title="Clôturer">
                                            <i class="bi bi-check-circle"></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-sm btn-outline-secondary" title="Déjà clôturée" disabled>
                                            <i class="bi bi-check-circle-fill"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<!-- DataTables -->
<script>
    $(document).ready(function () {
        $('#affectationTable').DataTable({
            language: {
                url: "//cdn.datatables.net/plug-ins/1.13.6/i18n/fr-FR.json"
            }
        });
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>