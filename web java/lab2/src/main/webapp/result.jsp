<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Результат проверки</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        tbody {
            font-family: monospace;
            color: black;
            font-size: 18px;
            border: 3px solid;
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            padding: 10px;
            border: 1px solid black;
            text-align: center;
        }
    </style>
</head>
<body>
<h3>Результаты:</h3>
<table id="resultTable">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Попадание</th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${param.x}</td>
                        <td>${param.y}</td>
                        <td>${param.r}</td>
                        <td><c:choose>
                                <c:when test="${param.hit}">Да</c:when>
                                <c:otherwise>Нет</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </tbody>
            </table>

<a href="controller">Вернуться на главную</a>
</body>
</html>
