<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<h2>couponGen</h2>
<form action="http://localhost:8080/first/couponGen.do" method="get">
	<input type="text" placeholder="PREFIX" name="prefix"/>
	<input type="text" placeholder="GROUP" name="group"/>
	<input type="submit" value="GENERATE">
</form>
<h2>couponList</h2>
<a href="http://localhost:8080/first/couponList.do">couponList</a>
<h2>couponStat</h2>
<a href="http://localhost:8080/first/couponStat.do">couponStat</a>
</body>
</html>