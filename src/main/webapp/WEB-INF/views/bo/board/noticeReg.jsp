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
		
		// 글 등록 버튼
		$("#NoticeInsBtn").on("click", function(){
			alert("글 등록 버튼 클릭 !!...")
			// 	$("#noticeRegFrm").submit();
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
	
	<form name="noticeRegFrm" id="noticeRegFrm" action="/common/board/noticeReg" method="POST" enctype="multipart/form-data">
		<input type="hidden" name="noticeSeq"/>
			<div class="contents">
				<div id="table">
					<table class="view_table">
						<tr>
							<th>제목</th>
							<td colspan="3">
								<input type="text" id="noticeTitle" name="noticeTitle" placeholder="제목을 입력해 주세요!..." class="w60" data-vd='{"type":"text","len":"1,100","req":true,"msg":"제목을 입력해 주세요"}'/>
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td colspan="3">
								<textarea name="content" cols="110" rows="15" placeholder="내용을 입력해 주세요!..." data-vd='{"type":"text","len":"1,100","req":true,"msg":"내용을 입력해 주세요"}'></textarea>
							</td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td id="fileTag">
								<input type="text" id="fileName" name="fileName" class="w60" readonly="readonly" />
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
			<a href="javascript:void(0);" id="NoticeInsBtn"  class="btn_gray btn_right02">글 등록</a>								
			<a href="javascript:void(0);" id="NoticeUpdBtn"  class="btn_gray btn_right">글 수정</a>								
			<a href="javascript:void(0);" id="NoticeCancelBtn"  class="btn_gray">글 목록</a>								
			<a href="javascript:void(0);" id="NoticeDelBtn"  class="btn_gray">글 삭제</a>								
		</div>
	</sec:authorize>
</div>


