<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		// 글 목록 버튼
		$("#companyCodeBtn").on("click", function(){
			location.href = "/common/board/companyCodePage";
		});
		
		// 글 등록 버튼
		$("#companyCodeSaveBtn").on("click", function(){
			if(confirm("등록 하시겠습니까?")){
				$("#companyCodeRegFrm").attr("action","/common/board/savecompanyCodeReg");
				$("#fileSeq").val("0"); // 기존 시퀀스 초기화
				var companyCodeSaveParam = {
					name : 'companyCodeRegFrm'
					,success: function(opt, result) {
		 				location.href="/common/board/companyCodePage";
					}
				}      
				AjaxUtil.files(companyCodeSaveParam);
			}
		});
		
		// 글 수정 버튼
		$("#companyCodeUpdBtn").on("click", function(){
			if(confirm("수정 하시겠습니까?")){
				$("#companyCodeRegFrm").attr("action","/common/board/updcompanyCodeReg");
				var companyCodeInsParam = {
					name : 'companyCodeRegFrm'
					,success: function(opt, result) {
		 				$("#companyCodeDetailFrm").submit();
					}
				}      
				AjaxUtil.files(companyCodeInsParam);
			}
		});
		
		// 글 취소 버튼
		$("#companyCodeCancelBtn").on("click", function(){
				$("#companyCodeDetailFrm").submit();
		});
		
	}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
	</div>
	
	<form id="companyCodeDetailFrm" method="post" action="/common/board/companyCodeDetailPage">
		<input type="hidden" name="companyCodeSeq" value="${companyCodeInfo.companyCodeSeq}"/>
	</form>
	
	<form name="companyCodeRegFrm" id="companyCodeRegFrm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="companyCodeSeq" value="${companyCodeInfo.companyCodeSeq}"/>
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<tr>
						<th>회원사(상호명)</th>
						<td colspan="3">
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요!..."  value="${companyCodeInfo.title}" class="w60" data-vd='{"type":"text","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>법인등록번호</th>
						<td colspan="3">
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요!..."  value="${companyCodeInfo.title}" class="w60" data-vd='{"type":"num","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>사업자등록번호</th>
						<td colspan="3">
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요!..."  value="${companyCodeInfo.title}" class="w60" data-vd='{"type":"num","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>회사대표번호</th>
						<td colspan="3">
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요!..."  value="${companyCodeInfo.title}" class="w60" data-vd='{"type":"num","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	
	<sec:authorize access="hasRole('SYSTEM')" >
		<div class="btn_wrap">
			<a href="javascript:void(0);" id="companyCodeBtn"  class="btn_gray">목록</a>								
		
			<c:choose>
				<c:when test="${!empty companyCodeInfo}">
					<a href="javascript:void(0);" id="companyCodeUpdBtn"  class="btn_gray btn_right">수정</a>
					<a href="javascript:void(0);" id="companyCodeCancelBtn"  class="btn_gray btn_right02">취소</a>		
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);" id="companyCodeSaveBtn"  class="btn_gray btn_right">등록</a>
				</c:otherwise>
			</c:choose>
		</div>
	</sec:authorize>
</div>


