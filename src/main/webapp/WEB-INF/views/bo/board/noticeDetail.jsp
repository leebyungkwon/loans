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
		
		//첨부파일명 보여주기
		$(".inputFile").on("change", function () {
			var fileVal 	= $(this).val().split("\\");
			var fileName 	= fileVal[fileVal.length - 1];
			$(this).prev().val(fileName);
		});
	
	}
	
	function filedown(fileSeq){
		var p = {
			url : '/common/fileDown'
			, contType: 'application/json; charset=UTF-8'
			, responseType: 'arraybuffer'
			, param : {
				fileSeq : fileSeq
			}
		}
		AjaxUtil.post(p);
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
				<div class="titlebox">
					<h3>${noticeInfo.title}</h3>
					<div class="date">
						${noticeInfo.regTimestamp}
					</div>
				</div>
				<div class="contbox">
					<pre>${noticeInfo.info}</pre>
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
								<a href="javascript:filedown('${file.fileSeq}')">${file.fileFullNm}</a>
							</td>
						</tr>
					</table>
				</div>
			
			<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
				<div class="btn_wrap">
					<a href="javascript:void(0);" id="noticeUpdBtn"  class="btn_gray btn_right02">글 수정</a>								
					<a href="javascript:void(0);" id="noticeDelBtn"  class="btn_gray btn_right">글 삭제</a>								
					<a href="javascript:void(0);" id="noticeBtn"  class="btn_gray">글 목록</a>								
				</div>
			</sec:authorize>
			
			</div>
		</div>
	</form>	
</div>

