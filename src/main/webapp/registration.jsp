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
<c:if test="${notCorrect!=null}">
    <p>You input not correct value login or password. Please try again.></p>
</c:if>
<c:if test="${exist!=null}">
    <p>You input exist user login. Please change your login <p>
</c:if>

<form action="${pageContext.request.contextPath}/registration" method="get">
    <div class="form-group" style="padding-top:5px">
        <label for="name">User Name:</label>
        <input type="text" class="form-control" id="name" name="name" required autocomplete="off">
    </div>

    <div class="form-group" style="padding-top:5px ">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" name="password" required autocomplete="off">
    </div>

    <div class="form-group" style="padding-top: 5px; padding-bottom: 5px">
        <label for="email">Email:</label>
        <input type="email" class="form-control" id="email" name="email" required autocomplete="off">
    </div>
    <button type="submit" class="btn btn-success"  >Registration</button>
</form>

<p>To return to the main page, click <a href="${pageContext.request.contextPath}/">here</a><br></p>

</body>
</html>
