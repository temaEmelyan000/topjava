<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<table border=1>
    <thead>
    <tr>
        <th>Id</th>
        <th>DateTime</th>
        <th>Description</th>
        <th colspan=2>Action</th>
    </tr>

    </thead>
    <tbody>

    <jsp:useBean id="meals" type="java.util.List" scope="request"/>

    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr class="${meal.exceed ? 'exceed' : 'normal'}">
            <td><c:out value="${meal.id}"/></td>
            <td><c:out value="${meal.dateTime}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=add">Add User</a></p>
</body>

</html>