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
	request.setCharacterEncoding("euc-kr");
%>

<!-- 게시글 작성한 데이터를 한번에 읽어드림 -->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" />
</jsp:useBean>

<%
	// 데이터베이스쪽으로 빈클래스를 넘겨줌
	BoardDAO bdao=new BoardDAO();

	// 데이터 저장 메소드를 호출
	bdao.insertBoard(boardbean);
	
	// 게시글 저장 후 전체게시글 보기
	response.sendRedirect("BoardList.jsp");

%>
	
</body>
</html>