<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<table>
<thead>
		<tr>
			<th>������ȣ</th>
			<th>�׷�</th>
			<th>���������</th>
			<th>��볯¥</th>
		</tr>
	</thead>
	
	<c:forEach var="item"  items="${result}">
		<tr>
			<td>${item.COUPON}</td>
			<td align="center">${item.CGROUP}</td>
			<td>${item.USEUSER}</td>
			<td>${item.USEDATE}</td>
		</tr>
	</c:forEach>
	

</table>
<center>
<form action="http://localhost:8080/first/couponList.do" method="get">
	<input type="text" id="isSearch" name="isSearch" placeholder="�׷�˻�"/>
	<input type="submit" value="�˻�">
</form>
</center>
<center>
<h2>${pagingHtml }</h2>
</center>

</body>
</html>