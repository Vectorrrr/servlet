<%--
  Created by IntelliJ IDEA.
  User: Ivan Gladush
  Date: 15.04.16
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload</title>
</head>
<body>
<h2>This file download page</h2>
Select the file and press send
<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
    <p>
        File for upload: <input type="file" name="upfile" multiple accept="text/plain"><br/>
        <input type="submit" value="Upload">
    <p/>
</form>
For go to the file download page click <a href="${pageContext.request.contextPath}/download"> here</a>

<p>To return to the main page, click <a href="${pageContext.request.contextPath}/">here</a><br></p>
</body>
</html>
