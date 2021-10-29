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
		var title = $("#title").val();
		var info = $("#info").val();
		
		if(WebUtil.isNull($("input[name='noticeDispCd']:checked").val())){
			alert("대상을 선택해 주세요.");
			return false;
		}
		if(WebUtil.isNull(title)){
			alert("제목을 입력해 주세요.");
			$("#title").focus();
			return false;
		}
		
		if(WebUtil.isNull(info)){
			alert("내용을 입력해 주세요.");
			$("#info").focus();
			return false;
		}
		
		if(confirm("등록하시겠습니까?")){
			$("#noticeRegFrm").attr("action","/common/board/saveNoticeReg");
			$("#fileSeq").val("0"); // 기존 시퀀스 초기화
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
		var title = $("#title").val();
		var info = $("#info").val();
		
		if(WebUtil.isNull(title)){
			alert("제목을 입력해 주세요.");
			$("#title").focus();
			return false;
		}
		
		if(WebUtil.isNull(info)){
			alert("내용을 입력해 주세요.");
			$("#info").focus();
			return false;
		}
		
		if(confirm("수정하시겠습니까?")){
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
	
	// 첨부파일 삭제
	$("#fileDelete").on("click", function(){
		$("#fileName").val("");
		
		// IE일 경우
		//$("#u_file").replaceWith( $("#u_file").clone(true) );
		$("#u_file").val("");
		$("#fileSeq").val("0");
	});
	
	// 첨부파일 찾기
	$("#fileSearch").on("click", function(){
		$("#u_file").click();
	});

	// 첨부파일 찾기시 file tag 실행
	$("#u_file").on("change", function(){
		if(WebUtil.isNotNull($("#u_file").val())){
			var ext = $(this).val().split(".").pop().toLowerCase();
			var excelYn = $(this).attr("data-excel");
			var fileSize = $("#u_file")[0].files[0].size;
			if(!Valid.boardFileCheck(fileSize, ext, excelYn)){
				return false;
			}
			
			var fileValue = $("#u_file").val().split("\\");
			var fileName = fileValue[fileValue.length-1];
			$("#fileName").val(fileName);
		}
	});
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
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
						<th>대상</th>
						<td>
							<c:forEach var="noticeCdList" items="${noticeCdList}">
							    <input type='radio' name='noticeDispCd' id="noticeDispCd${noticeCdList.codeDtlCd}" value='${noticeCdList.codeDtlCd}' <c:if test="${noticeInfo.noticeDispCd eq noticeCdList.codeDtlCd}">checked="checked"</c:if>/><label for="noticeDispCd${noticeCdList.codeDtlCd}">${noticeCdList.codeDtlNm}</label>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" id="title" name="title" placeholder="제목을 입력해 주세요."  value="${noticeInfo.title}" class="w60" maxlength="53" data-vd='{"type":"text","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<textarea id="info" name="info" cols="110" rows="30" placeholder="내용을 입력해 주세요.">${noticeInfo.info}</textarea>
						</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td id="fileTag">
							<input type="text" id="fileName" name="fileName" class="w60" readonly="readonly" value="${file.fileFullNm}"/>
							<input type="hidden" id="fileSeq"  name="fileSeq" value=" ${noticeInfo.fileSeq}"/>
							<a href="javascript:void(0);" class="btn_Lgray btn_small" id="fileDelete">삭제</a>
							<a href="javascript:void(0);" class="btn_gray btn_small" id="fileSearch">파일찾기</a>
							<input type="file" id="u_file" class="" name="files" multiple="multiple" style="display:none;">
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	
	<div class="btn_wrap">
		<a href="javascript:void(0);" id="noticeBtn"  class="btn_gray">목록</a>
		<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
			<c:choose>
				<c:when test="${!empty noticeInfo}">
					<a href="javascript:void(0);" id="noticeUpdBtn"  class="btn_blue btn_right">수정</a>
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);" id="noticeSaveBtn"  class="btn_blue btn_right">등록</a>
				</c:otherwise>
			</c:choose>
		</sec:authorize>
	</div>
</div>


