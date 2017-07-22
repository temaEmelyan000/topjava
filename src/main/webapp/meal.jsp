<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>


<%--<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>--%>


<form action="${pageContext.request.contextPath}/meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    dateTime <input type="datetime-local" name="dateTime" value="${meal.dateTime}"><br>
    Description <input name="description" value="${meal.description}"><br>
    Calories <input type="number" name="calories" value="${meal.calories}"><br>
    <input type="submit" value="Submit">
</form>

</body>
</html>
