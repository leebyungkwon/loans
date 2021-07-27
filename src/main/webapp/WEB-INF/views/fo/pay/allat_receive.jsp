<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
	System.out.println("::::: allat_receive.jsp :::::");	

	// 결과값
	String sResultCd  = request.getParameter("allat_result_cd");
	String sResultMsg = request.getParameter("allat_result_msg");
	String sEncData   = request.getParameter("allat_enc_data");
	
	System.out.println("::::: allat_receive.jsp >> sResultCd :::::"+sResultCd);
	System.out.println("::::: allat_receive.jsp >> sResultMsg :::::"+sResultMsg);
	System.out.println("::::: allat_receive.jsp >> sEncData :::::"+sEncData);
	
	// 결과값 Return
	out.println("<script>");
	out.println("alert('allat_receive.jsp 진입 확인용 alert');");
	out.println("if(window.opener != undefined) {");
	out.println("	opener.result_submit('" + sResultCd + "','" + sResultMsg + "','" + sEncData + "');");
	out.println("	window.close();");
	out.println("} else {");
	out.println("	parent.result_submit('" + sResultCd + "','" + sResultMsg + "','" + sEncData + "');");
	out.println("}");
	out.println("</script>");
%>