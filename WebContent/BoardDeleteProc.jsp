<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

<%
	String pass = request.getParameter("password");
	int num=Integer.parseInt(request.getParameter("num"));
	
	// �����ͺ��̽� ����
	BoardDAO bdao = new BoardDAO();
	String password=bdao.getPass(num);
	
	// ���� �н����尪�� delete form���� �ۼ��� �н����带 ��
	if(pass.equals(password)){
		
		// �н����尡 �� �� ���ٸ�
		bdao.deleteBoard(num); 
		response.sendRedirect("BoardList.jsp");
	}
	else{
%>
	<script>
		alert("�н����尡 Ʋ���� ���� �� �� �����ϴ�. �н����带 Ȯ�����ּ���");
		history.go(-1);
	</script>
<% 
	}
	
%>

</body>
</html>