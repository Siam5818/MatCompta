<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Suivi des états - MatCompta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">
        <h3 class="fw-bold text-primary mb-4"><i class="bi bi-graph-up me-2"></i> État & Suivi des matériels</h3>

        <!-- Résumé global -->
        <div class="row g-4 mb-4">
            <c:forEach var="etat" items="${etatMaterielList}">
                <div class="col-md-6 col-lg-4">
                    <div class="card shadow-sm border-0">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="fw-bold">${etat}</h6>
                                <small class="text-muted">Matériel(s) :</small>
                            </div>
                            <span class="badge bg-primary fs-5">
                                    ${nombreParEtat[etat]}
                            </span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Tableau détaillé -->
        <div class="card shadow-sm">
            <div class="card-body table-responsive">
                <table class="table table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Libellé</th>
                        <th>Catégorie</th>
                        <th>État</th>
                        <th>Date d'achat</th>
                        <th>Emplacement</th>
                        <th class="text-end">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="m" items="${listeMateriels}">
                        <tr>
                            <td>${m.libelle}</td>
                            <td>${m.categorie.nom}</td>
                            <td>
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${m.etat == 'EN_SERVICE'}">bg-success</c:when>
                                            <c:when test="${m.etat == 'A_REPARER'}">bg-warning</c:when>
                                            <c:when test="${m.etat == 'HORS_SERVICE'}">bg-danger</c:when>
                                            <c:otherwise>bg-secondary</c:otherwise>
                                        </c:choose>">
                                            ${m.etat}
                                    </span>
                            </td>
                            <td>${m.dateAchat}</td>
                            <td>${m.emplacement}</td>
                            <td class="text-end">
                                <a href="materiel-servlet?action=edit&id=${m.id}" class="btn btn-sm btn-outline-primary" title="Modifier">
                                    <i class="bi bi-pencil-fill"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>