<%--
  Created by IntelliJ IDEA.
  User: Ivan Gladush
  Date: 15.04.16
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
<h1>Main Page</h1>

<c:choose>
    <c:when test="${ empty name}">
        I see!  You don't have a name.. well.. Hello no name
    </c:when>
    <c:otherwise>
       Hello my dear friend ${name}
    </c:otherwise>
</c:choose>

<p> This site is intended for important things</p>
<p>
    <h2>To go to the file upload page click  <a href="${pageContext.request.contextPath}/upload">here</a></h2>
    <h2>To download a file click <a href="${pageContext.request.contextPath}/download">here</a></h2>

<c:if test="${name != \"\"}">
    <form method="post" action="${pageContext.request.contextPath}/">
        <h2>For leaving the page, press </h2><button type="submit" class="btn btn-info">Log out</button>
    </form>
</c:if>
</p>

</body>
</html>
