<%--
  Created by IntelliJ IDEA.
  User: Ivan Gladush
  Date: 18.04.16
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Download Page</title>
</head>
<body>
<form action="http://localhost:8080/download" method="get" >
    <p>
        Для поиска файла введите строку
    <h3> <input name="name" type="text"><br></h3>
    <h3><input type="submit" value="Поиск"><br></h3>

    <p/>
</form>

<c:forEach  items="${files}" var="num">
    <form action="http://localhost:8080/download" method="get" enctype="multipart/form-data">
        <p>
        <h3> <input name="fileName" type="hidden" value="${num}"  >${num}</input><br></h3>
        <input name="hid" value="1" type="hidden">
        <h3><input type="submit" value="Загрузить"><br></h3>

        <p/>
    </form>

</c:forEach>

</body>
</html>
