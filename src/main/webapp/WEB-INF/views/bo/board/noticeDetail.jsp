<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<style>
table th {border-bottom: none;}
table td {border-bottom: none;}
.notice_view .titlebox:not(:first-child) {border-top: none;}
</style>

<script type="text/javascript">
function pageLoad(){
	// 글 목록 버튼
	$("#noticeBtn").on("click", function(){
		//location.href = "/common/board/noticePage";
		history.back();
	});
	
	// 글 수정 버튼
	$("#noticeUpdBtn").on("click", function(){
			$("#noticeDetailFrm").submit();
	});
	
	// 글 삭제 버튼
	$("#noticeDelBtn").on("click", function(){
		if(confirm("글을 삭제 하시겠습니까?")){
			var noticeSeq = $("#noticeSeq").val();
			var param = {
				'noticeSeq' : noticeSeq
			}
			var p = {
				param: param
				,url: "/common/board/delNoticeReg"
				,success: function(opt, result) {
					if(result > 0) {
					alert("삭제를 실패하셨습니다.");
					} else {
					alert("삭제를 성공적으로 완료하였습니다.");
					location.href = "/common/board/noticePage";
					}             
				}
			}
			AjaxUtil.post(p);
		}
	});
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
	</div>

	<form name="noticeDetailFrm" id="noticeDetailFrm" action="/common/board/noticeRegInsPage" method="POST" enctype="multipart/form-data" >
		<input type="hidden" name="noticeSeq" id="noticeSeq" value="${noticeInfo.noticeSeq}"/>
		
		<div class="contents">
			<div class="notice_view">
				<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
					<div class="titlebox">
						<table>
							<colgroup>
								<col width="150"/>
								<col width="*"/>
							</colgroup>
							<tr>
								<th class="acenter">대상</th>
								<td class="aleft" colspan="3">${noticeInfo.noticeDispCdNm}</td>
							</tr>
						</table>
					</div>
				</sec:authorize>
				<div class="titlebox">
					<table>
						<colgroup>
							<col width="150"/>
							<col width="*"/>
						</colgroup>
						<tr>
							<th class="acenter">제목</th>
							<td class="aleft" colspan="3">
								${noticeInfo.title}
							</td>
						</tr>
					</table>
				</div>
				<div class="titlebox">
					<table>
						<colgroup>
							<col width="150"/>
							<col width="*"/>
						</colgroup>
						<tr>
							<th class="acenter">작성일</th>
							<td class="aleft" colspan="3">
								${noticeInfo.regTimestamp}
							</td>
						</tr>
					</table>
				</div>
				<div class="contbox" <c:if test="${!empty file }">style="border-bottom: none;"</c:if>> 
					<pre>${noticeInfo.info}</pre>
				</div>
				<c:if test="${!empty file }">
					<div class="titlebox">
						<table>
							<colgroup>
								<col width="150"/>
								<col width="*"/>
							</colgroup>
							<tr>
								<th class="acenter" style="border-bottom: 1px solid #e1e1e1;">첨부파일</th>
								<td class="aleft" colspan="3" style="border-bottom: 1px solid #e5e5e5;">
									<a href="/common/fileDown?fileSeq=${file.fileSeq}">${file.fileFullNm}</a>
								</td>
							</tr>
						</table>
					</div>
				</c:if>
				<div class="btn_wrap">
					<a href="javascript:void(0);" id="noticeBtn"  class="btn_gray">목록</a>
					<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
						<a href="javascript:void(0);" id="noticeUpdBtn"  class="btn_blue btn_right02">수정</a>
						<a href="javascript:void(0);" id="noticeDelBtn"  class="btn_black btn_right">삭제</a>
					</sec:authorize>
				</div>
			</div>
		</div>
	</form>
</div>

