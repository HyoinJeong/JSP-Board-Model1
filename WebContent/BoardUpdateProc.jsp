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
<!-- ����ڵ����͸� �о�帮�� ��Ŭ���� ���� -->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" />
</jsp:useBean>
<%
	// �����ͺ��̽��� ����
	BoardDAO bdao = new BoardDAO();
	// �ش� �Խñ��� �н����尪�� ����
	String pass = bdao.getPass(boardbean.getNum());
	
	// ���� �н����尪�� update�� �ۼ��� password���� ������ ��
	if(pass.equals(boardbean.getPassword())){
		// ������ �����ϴ� �޼ҵ� ȣ��
		bdao.updateBoard(boardbean);
		// ������ �Ϸ�Ǹ� ��ü �Խñ� ����
		response.sendRedirect("BoardList.jsp");
		
	}else{ // �н����尡 Ʋ���ٸ�
%>
		<script type="text/javascript">
			alert("�н����尡 ��ġ ���� �ʽ��ϴ�. �ٽ� Ȯ�� �� �������ּ���");
			history.go(-1);
		</script>
<%		
	}
%>

</body>
</html>