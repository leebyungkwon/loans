<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		// 글 목록 버튼
		$("#noticeBtn").on("click", function(){
			location.href = "/common/board/noticePage";
		});
		
		// 글 등록 버튼
		$("#noticeSaveBtn").on("click", function(){
			if(confirm("글을 등록 하시겠습니까?")){
				$("#noticeRegFrm").attr("action","/common/board/saveNoticeReg");
				var noticeSaveParam = {
					name : 'noticeRegFrm'
					,success: function(opt, result) {
		 				location.href="/common/board/noticePage";
					}
				}      
				AjaxUtil.files(noticeSaveParam);
			}
		});
		
		// 글 수정 버튼
		$("#noticeUpdBtn").on("click", function(){
			if(confirm("글을 수정 하시겠습니까?")){
				$("#noticeRegFrm").attr("action","/common/board/updNoticeReg");
				var noticeInsParam = {
					name : 'noticeRegFrm'
					,success: function(opt, result) {
		 				$("#noticeDetailFrm").submit();
					}
				}      
				AjaxUtil.files(noticeInsParam);
			}
		});
		
		// 글 취소 버튼
		$("#noticeCancelBtn").on("click", function(){
				$("#noticeDetailFrm").submit();
		});
		
		// 첨부파일 삭제
		$("#fileDelete").on("click", function(){
			$("#fileName").val("");
			
			// IE일 경우
			//$("#u_file").replaceWith( $("#u_file").clone(true) );
			$("#u_file").val("");
		});
		
		// 첨부파일 찾기
		$("#fileSearch").on("click", function(){
			$("#u_file").click();
		});

		// 첨부파일 찾기시 file tag 실행
		$("#u_file").on("change", function(){
			var fileValue = $("#u_file").val().split("\\");
			var fileName = fileValue[fileValue.length-1];
			$("#fileName").val(fileName);
		});
		
	}

</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>공지사항</h2>
		</div>
	</div>
	
	<form id="noticeDetailFrm" method="post" action="/common/board/noticeDetailPage">
		<input type="hidden" name="noticeSeq" value="${noticeInfo.noticeSeq}"/>
	</form>
	
	<form name="noticeRegFrm" id="noticeRegFrm" method="POST" enctype="multipart/form-data">
	<input type="hidden" name="noticeSeq" value="${noticeInfo.noticeSeq}"/>
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<tr>
						<th>제목</th>
						<td colspan="3">
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요!..."  value="${noticeInfo.title}" class="w60" data-vd='{"type":"text","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td colspan="3">
							<textarea name="info" cols="110" rows="30" placeholder="내용을 입력해 주세요!..."  data-vd='{"type":"text","len":"1,100","req":true,"msg":"내용을 입력해 주세요"}'>${noticeInfo.info}</textarea>
						</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td id="fileTag">
							<input type="text" id="fileName" name="fileName" class="w60" readonly="readonly" value="${file.fileFullNm}" />
							<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
							<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
							<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	
	<sec:authorize access="hasRole('SYSTEM')" >
		<div class="btn_wrap">
			<a href="javascript:void(0);" id="noticeBtn"  class="btn_gray">글 목록</a>								
		
			<c:choose>
				<c:when test="${!empty noticeInfo}">
					<a href="javascript:void(0);" id="noticeUpdBtn"  class="btn_gray btn_right">글 수정</a>
					<a href="javascript:void(0);" id="noticeCancelBtn"  class="btn_gray btn_right02">글 취소</a>		
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);" id="noticeSaveBtn"  class="btn_gray btn_right">글 등록</a>
				</c:otherwise>
			</c:choose>
		</div>
	</sec:authorize>
</div>


