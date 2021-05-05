<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
	}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
	</div>

	<div class="contents">
		<div class="notice_view">
			<div class="titlebox">
				<h3>[필독] 리스할부 모집인 등록 파일명 규칙</h3>
				<div class="date">
					2021.03.11
				</div>
			</div>
				
			<div class="contbox">
				1. 엑셀파일명 등록 규칙<br /><br />
				리스할부 모집인 등록 시 파일이름 (21자리)<br /><br />
				업무구분코드(3) + 파일특성(1) + 일련번호(2) + “_D”(2) + 자료송부일(6) + “_”(1) + 기관코드(2) + “.”(1) + 확장자(3)<br /><br />

				업무구분코드(3자리) : cls<br />
				파일특성 (1자리) : 여신금융사 기준 송부시  u, 수신시  s<br />
				일련번호 (2자리) : 01  등록/해지요청<br />
				자료송부일 (6자리) : YYMMDD<br />
				기관코드(2자리) : 여신금융회사 코드 (여신금융사별 코드는 공지사항에서 확인 가능)<br />
				확장자(3자리) : txt<br /><br />

				2. 사진파일명 등록 규칙<br />
				사진 파일이름 (21자리)<br /><br />

				업무구분코드(3) + 파일특성(1) + 일련번호(2) + “_D”(2) + 자료송부일(6) + “_”(1) + 기관코드(2) + “.”(1) + 확장자(3)<br /><br />

				업무구분코드(3자리) : cls<br />
				파일특성 (1자리) : u<br />
				일련번호 (2자리) : 02<br />
				자료송부일 (6자리) : YYMMDD<br />
				기관코드(2자리) : 여신금융회사 코드 (여신금융사별 코드는 공지사항에서 확인 가능)<br />
				확장자(3자리) : zip
			</div>
			<div class="btn_wrap">
				<a href="#" class="btn_gray">목록</a>								
			</div>
		</div>
	</div>
</div>

