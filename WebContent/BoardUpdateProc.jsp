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
<!-- 사용자데이터를 읽어드리는 빈클래스 생성 -->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" />
</jsp:useBean>
<%
	// 데이터베이스에 연결
	BoardDAO bdao = new BoardDAO();
	// 해당 게시글의 패스워드값을 얻어옴
	String pass = bdao.getPass(boardbean.getNum());
	
	// 기존 패스워드값고 update시 작성한 password값이 같은지 비교
	if(pass.equals(boardbean.getPassword())){
		// 데이터 수정하는 메소드 호출
		bdao.updateBoard(boardbean);
		// 수정이 완료되면 전체 게시글 보기
		response.sendRedirect("BoardList.jsp");
		
	}else{ // 패스워드가 틀리다면
%>
		<script type="text/javascript">
			alert("패스워드가 일치 하지 않습니다. 다시 확인 후 수정해주세요");
			history.go(-1);
		</script>
<%		
	}
%>

</body>
</html>