<%--
  Created by IntelliJ IDEA.
  User: igladush
  Date: 20.04.16
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<h1>Login to your account before you download the files</h1>
<form action="${pageContext.request.contextPath}/login" method="get">
    <div class="form-group">
        <label for="name">User Name:</label>
        <input type="text" class="form-control" id="name" name="name" required>
    </div>
    <div class="form-group" style="padding-top:5px">
        <label for="password">Password:</label>
        <input type="password" class="form-control" id="password" name="password" required autocomplete="off">
    </div>
    <button type="submit" class="btn btn-default">Login</button>
</form>
To return to the main page, click <a href="${pageContext.request.contextPath}/">here</a>
<p>To return to the main page, click here<a href="${pageContext.request.contextPath}/">here</a><br></p>
</body>
</html>
