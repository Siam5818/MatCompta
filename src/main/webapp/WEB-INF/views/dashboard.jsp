<%@ page session="true" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" pattern="HH" var="hour"/>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - MatCompta</title>

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <!-- jQuery & DataTables -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <!-- Ton style personnalisé -->
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">
</head>

<body class="d-flex flex-column min-vh-100 overflow-x-hidden dashboard-body">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;" class="flex-grow-1">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="dashboard-content container mt-4">

        <!-- Header utilisateur -->
        <div class="dashboard-header mb-4 p-4 rounded shadow-sm bg-light">
            <c:choose>
                <c:when test="${hour lt 12}">
                    <h3 class="mb-0">Bonjour, ${sessionScope.utilisateur.nomComplet}</h3>
                </c:when>
                <c:when test="${hour lt 18}">
                    <h3 class="mb-0">Bon après-midi, ${sessionScope.utilisateur.nomComplet}</h3>
                </c:when>
                <c:otherwise>
                    <h3 class="mb-0">Bonsoir, ${sessionScope.utilisateur.nomComplet}</h3>
                </c:otherwise>
            </c:choose>
            <p class="mb-0 text-muted">Bienvenue sur votre tableau de bord MatCompta.</p>
        </div>

        <!-- Cartes résumées -->
        <div class="row g-4">
            <div class="col-md-4">
                <div class="card card-matcompta border-primary">
                    <div class="card-body">
                        <h5 class="card-title"><i class="bi bi-laptop me-2"></i>Matériels</h5>
                        <p class="card-text">${totalMateriels} enregistrés</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card card-matcompta border-success">
                    <div class="card-body">
                        <h5 class="card-title"><i class="bi bi-person-check me-2"></i>Utilisateurs</h5>
                        <p class="card-text">${totalUtilisateurs} actifs</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Graphique des états et liste matériels -->
        <div class="row g-4 mt-5">
            <div class="col-md-6">
                <div class="card shadow-sm" style="height: 400px;">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">État des matériels</h5>
                        <div class="flex-grow-1">
                            <canvas id="etatMaterielChart" style="max-height:300px;"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card shadow-sm" style="height: 400px;">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Matériels récents</h5>
                        <div class="table-responsive flex-grow-1 overflow-auto">
                            <table id="materielTable" class="table table-striped table-bordered mb-0">
                                <thead>
                                <tr><th>Nom</th><th>Type</th><th>État</th></tr>
                                </thead>
                                <tbody>
                                <c:forEach var="m" items="${listeMateriels}">
                                    <tr>
                                        <td>${m.libelle}</td>
                                        <td>${m.reference}</td>
                                        <td>
                                                <span class="badge
                                                    <c:choose>
                                                        <c:when test="${m.etat == 'EN_SERVICE'}">bg-success</c:when>
                                                        <c:when test="${m.etat == 'A_REPARER'}">bg-warning text-dark</c:when>
                                                        <c:when test="${m.etat == 'HORS_SERVICE'}">bg-danger</c:when>
                                                        <c:otherwise>bg-secondary</c:otherwise>
                                                    </c:choose>">
                                                        ${m.etat}
                                                </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Graphique des catégories et affectations -->
        <div class="row g-4 mt-5">
            <div class="col-md-6">
                <div class="card shadow-sm" style="height: 400px;">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Répartition par catégorie</h5>
                        <div class="flex-grow-1">
                            <canvas id="categorieChart" style="max-height:280px;"></canvas>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card shadow-sm" style="height: 400px;">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Affectations récentes</h5>
                        <div class="table-responsive flex-grow-1 overflow-auto">
                            <table class="table table-striped table-bordered mb-0">
                                <thead>
                                <tr><th>Matériel</th><th>Attribué à</th><th>Date</th></tr>
                                </thead>
                                <tbody>
                                <c:forEach var="a" items="${affectationsRecentes}">
                                    <tr>
                                        <td>${a.materiel.libelle}</td>
                                        <td>${a.employe.nomComplet}</td>
                                        <td>${a.dateAffectation}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- JS dynamique -->
<script>
    const etatLabels = [<c:forEach var="e" items="${etatMaterielList}">'${e}',</c:forEach>];
    const etatValues = [<c:forEach var="e" items="${etatMaterielList}">${nombreParEtat[e]},</c:forEach>];

    new Chart(document.getElementById('etatMaterielChart'), {
        type: 'bar',
        data: {
            labels: etatLabels,
            datasets: [{
                label: 'Nombre de matériels',
                data: etatValues,
                backgroundColor: ['#0d6efd', '#6f42c1', '#198754', '#dc3545', '#ffc107', '#0dcaf0']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } },
            plugins: { legend: { display: false } }
        }
    });

    const catLabels = [<c:forEach var="c" items="${listeCategories}">'${c.nom}',</c:forEach>];
    const catValues = [<c:forEach var="c" items="${listeCategories}">${nombreParCategorie[c.nom]},</c:forEach>];

    new Chart(document.getElementById('categorieChart'), {
        type: 'pie',
        data: {
            labels: catLabels,
            datasets: [{
                data: catValues,
                backgroundColor: ['#0d6efd', '#ffc107', '#6f42c1', '#198754', '#dc3545']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
</script>
</body>
</html>