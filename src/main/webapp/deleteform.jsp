<%@page import="kr.co.itcen.guestbook.dao.GuestbookDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String password = request.getParameter("password");
	Long no = Long.parseLong(request.getParameter("no"));
	
	if(password !=null){
		Boolean check;
		check = new GuestbookDao().delete(no, password);
		if(check){
			response.sendRedirect(request.getContextPath());
		}
	}	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>방명록</title>
</head>
<body>
	<form method="post" action="<%=request.getContextPath() %>/deleteform.jsp">
		<input type='hidden' name="no" value="<%=no%>">
		<table>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="password">
				<input type="hidden" name="no" value="<%=no%>"></td>
				<td><input type="submit" value="확인"></td>
				
				<td><a href="<%=request.getContextPath() %>">메인으로 돌아가기</a></td>
			</tr>
		</table>
	</form>
</body>
</html>