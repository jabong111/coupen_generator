<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>couponStat</title>
</head>
<body>

<h1>���� ���</h1>
<table border="1">
<thead>
	<tr>
		<td>
			�׷�
		</td>
		<td>
			��� / ��ü
		</td>
	</tr>
</thead>
<c:forEach items="${result }" var="item">
	<tr align="center">
		<td>
			${item.CGROUP }
		</td>
		<td>
			${item.COUNT } /  10000
		</td>
	</tr>

</c:forEach>
</table>



</body>
</html>