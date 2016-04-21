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
<form action="${pageContext.request.contextPath}/download" method="get" >
    <p>
       For search file input string here
    <h3> <input name="name" type="text"><br></h3>
    <h3><input type="submit" value="Search"><br></h3>

    <p/>
</form>

<c:forEach  items="${files}" var="num">
    <form action="${pageContext.request.contextPath}/download" method="get" enctype="multipart/form-data">

        <h3> <input name="fileName" type="hidden" value="${num}"  >${num}</input><br></h3>
        <input name="hid" value="1" type="hidden">
        <h3><input type="submit" value="Upload"><br></h3>

    </form>

</c:forEach>

For go to the file upload page click <a href="${pageContext.request.contextPath}/upload"> here</a>

</body>
</html>
