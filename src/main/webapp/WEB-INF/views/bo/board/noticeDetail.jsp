<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){

		// 글 목록 버튼
		$("#NoticeCancelBtn").on("click", function(){
			location.href = "/common/board/noticePage";
		});
		
		// 글 수정 버튼
		$("#NoticeUpdBtn").on("click", function(){
			alert("글 수정 버튼 클릭 !!...")
			// 	$("#noticeRegFrm").submit();
		});
		
		// 글 삭제 버튼
		$("#NoticeDelBtn").on("click", function(){
			alert("글 삭제 버튼 클릭 !!...")
		// 	$("#noticeRegFrm").submit();
		});
		
		//첨부파일명 보여주기
		$(".inputFile").on("change", function () {
			var fileVal 	= $(this).val().split("\\");
			var fileName 	= fileVal[fileVal.length - 1];
			$(this).prev().val(fileName);
		});
		
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
			
			<div class="titlebox">
				<table>
					<colgroup>
						<col width="150"/>
						<col width="*"/>
					</colgroup>
					<tr>
						<th class="acenter">첨부 파일 : </th>
						<td class="aleft" colspan="3">
							<a href="javascript:filedown('${file.fileSeq}')">안뇽안뇽.xlsx</a>
<%-- 						<a href="javascript:filedown('${file.fileSeq}')">${file.fileFullNm}</a> --%>
						</td>
					</tr>
				</table>
			</div>
			
			<sec:authorize access="hasRole('SYSTEM')" >
				<div class="btn_wrap">
					<a href="javascript:void(0);" id="NoticeUpdBtn"  class="btn_gray btn_right02">글 수정</a>								
					<a href="javascript:void(0);" id="NoticeDelBtn"  class="btn_gray btn_right">글 삭제</a>								
					<a href="javascript:void(0);" id="NoticeCancelBtn"  class="btn_gray">글 목록</a>								
				</div>
			</sec:authorize>
			
		</div>
	</div>
</div>

