<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Utilisateurs - MatCompta</title>

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/style.css' />">

    <!-- DataTables -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css"/>
</head>

<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<div style="margin-left:220px;">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>

    <div class="container mt-4">

        <!-- Titre et bouton -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="fw-bold text-primary"><i class="bi bi-people me-2"></i> Utilisateurs</h3>
            <a href="utilisateur-servlet?action=add" class="btn btn-sm btn-primary">
                <i class="bi bi-plus-lg me-1"></i> Ajouter
            </a>
        </div>

        <!-- Tableau -->
        <div class="card shadow-sm">
            <div class="card-body table-responsive">
                <table class="table table-bordered table-hover align-middle" id="utilisateurTable">
                    <thead class="table-light">
                    <tr>
                        <th>Nom</th>
                        <th>Email</th>
                        <th>Téléphone</th>
                        <th>Rôle</th>
                        <th class="text-end">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${listeUtilisateurs}">
                        <tr>
                            <td>${user.nomComplet}</td>
                            <td>${user.email}</td>
                            <td>${user.phone}</td>
                            <td>
                                    <span class="badge
                                        <c:choose>
                                            <c:when test="${user.role == 'ADMIN'}">bg-danger</c:when>
                                            <c:when test="${user.role == 'COMPTABLE'}">bg-warning text-dark</c:when>
                                            <c:otherwise>bg-secondary</c:otherwise>
                                        </c:choose>">
                                            ${user.role == 'ADMIN' ? "Admin" : (user.role == 'COMPTABLE' ? "Comptable" : "Employé")}
                                    </span>
                            </td>
                            <td class="text-end">
                                <a href="utilisateur-servlet?action=edit&id=${user.id}" class="btn btn-sm btn-outline-primary me-1" title="Modifier">
                                    <i class="bi bi-pencil-fill"></i>
                                </a>
                                <c:choose>
                                    <c:when test="${user.role == 'ADMIN'}">
                                        <a href="#" class="btn btn-sm btn-outline-danger disabled"
                                           title="Impossible de supprimer un administrateur"
                                           tabindex="-1" aria-disabled="true" style="pointer-events: none;">
                                            <i class="bi bi-trash-fill"></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="utilisateur-servlet?action=delete&id=${user.id}" class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Confirmer la suppression de ${user.nomComplet} ?');"
                                           title="Supprimer">
                                            <i class="bi bi-trash-fill"></i>
                                        </a>
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

<!-- Script DataTables -->
<script>
    $(document).ready(function () {
        $('#utilisateurTable').DataTable({
            language: {
                url: "//cdn.datatables.net/plug-ins/1.13.6/i18n/fr-FR.json"
            }
        });
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>