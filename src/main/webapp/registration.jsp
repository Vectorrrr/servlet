<%--
  Created by IntelliJ IDEA.
  User: igladush
  Date: 20.04.16
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Registration Page</title>
</head>
<body>
<h1>Please register</h1>
<c:if test="${name!=null}">
    <p>You input not exist value login or password: <c:out value="${name}"/><p>
</c:if>

<form action="${pageContext.request.contextPath}/registration" method="get">
    <div class="form-group">
        <label for="name">User Name:</label>
        <input type="text" class="form-control" id="name" name="name" required>
    </div>
    <br/>
    <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" name="password" required>
    </div>
    <button type="submit" class="btn btn-success">Login</button>
</form>

<p>To return to the main page, click <a href="${pageContext.request.contextPath}/">here</a><br></p>

</body>
</html>
