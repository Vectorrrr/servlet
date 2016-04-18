<%--
  Created by IntelliJ IDEA.
  User: root
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
<h2>Это страница загрузки файла</h2>
Укажите файл и нажмите отправить
<form action="http://localhost:8080/upload" method="post" enctype="multipart/form-data">
    <p>
    <h3> <input name="data" type="file"><br></h3>
    <h3><input type="submit" value="Отправить"><br></h3>

    <p/>
</form>
</body>
</html>
