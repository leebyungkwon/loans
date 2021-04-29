<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad(){
	//첨부파일명 보여주기
	$(".inputFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
	});
}

//수정
function goUserRegInfoUpdt() {
	var p = {
		  name 		: "userRegInfoUpdFrm"
		, success 	: function (opt,result) {
			alert("호잇");
 	    }
	}
	AjaxUtil.files(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록(법인)</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="fileGrpSeq" value="${userRegInfo.fileSeq }"/>
		
		<div class="contents">
			<h3>등록정보</h3>
			<div id="table">
				<table class="view_table">
					<tr>
						<th>회원사</th>
						<td>볼보파이낸셜</td>
						<th>담당자</th>
						<td>담당자1 (<a href="abc@gmail.com">abc@gmail.com</a>, 010-12345678)</td>
					</tr>
					<tr>
						<th>모집인 상태</th>
						<td>${userRegInfo.plRegStat } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5">이력보기</a></td>
						<th>결제여부</th>
						<td>${userRegInfo.plPayStat } (국민카드 / 2021.10.20)</td>
					</tr>
					<tr>
						<th>처리상태</th>
						<td colspan="3">${userRegInfo.plStat }</td>
					</tr>
					<tr>
						<th>모집인 분류</th>
						<td colspan="3">${userRegInfo.plClass }</td>
					</tr>
					<tr>
						<th>신규경력구분</th>
						<td colspan="3">신규</td>
					</tr>
					<tr>
						<th>금융상품유형</th>
						<td colspan="3">${userRegInfo.plProduct }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td><input type="text" name="plMName" class="w100" value="${userRegInfo.plMName }"></td>
						<th>주민번호</th>
						<td><input type="text" name="plMZId" class="w100" value="${userRegInfo.plMZId }"></td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3"><input type="text" name="plCellphone" class="w100" value="${userRegInfo.plCellphone }"></td>
					</tr>
					<tr>
						<th>주소</th>
						<td colspan="3"><input type="text" name="addr" class="w100" value="${userRegInfo.addr }"></td>
					</tr>
					<tr>
						<th>교육이수번호</th>
						<td colspan="3"><input type="text" name="plEduNo" class="w100" value="${userRegInfo.plEduNo }"></td>
					</tr>
					<tr>
						<th>경력시작일</th>
						<td><input type="text" name="careerStartDate" class="w100" value="${userRegInfo.careerStartDate }"></td>
						<th>경력종료일</th>
						<td><input type="text" name="careerEndDate" class="w100" value="${userRegInfo.careerEndDate }"></td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td colspan="3"><input type="text" name="comContDate" class="w50" value="${userRegInfo.comContDate }"></td>
					</tr>
					<tr>
						<th>위탁예정기간</th>
						<td colspan="3"><input type="text" name="entrustDate" class="w50" value="${userRegInfo.entrustDate }"></td>
					</tr>
				</table>
			</div>
	
			<h3>첨부서류</h3>
			<div id="table02">
				<table class="view_table">
					<colgroup>
						<col width="50%"/>
						<col width="50%"/>
					</colgroup>
					<tr>
						<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일*</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="identityFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#identityFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">교육과정 이수확인서 (경력) *</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="eduCompleteFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="2"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#eduCompleteFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">경력증명서*</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="careerFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="3"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10" onclick="">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#careerFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">교육과정 인증서 (신규)*</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="eduVeriFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="4"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#eduVeriFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형, 내용에 대한 설명자료*</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="explainFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="5"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#explainFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">결격사유 확인서 (파산, 피한정후견인등)*</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="disqFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="6"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#disqFile').click();">파일찾기</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">대리인 신청 위임장(위임인 인간날인) 및 위임인 인감증명서</th>
						<td>
							<input type="text" class="w50 file_input" readonly disabled>
							<input type="file" name="files" id="authFile" class="inputFile" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="7"/>
							<a href="javascript:void(0);" class="btn_gray btn_del mgl10">삭제</a>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#authFile').click();">파일찾기</a>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_wrap">
				<a href="history.back();" class="btn_gray">이전</a>
				<c:if test="${userRegInfo.plRegStat eq '1' }"> 
					<!-- 승인요청상태가 아닐 때만 수정 가능 -->
					<a href="javascript:goUserRegInfoUpdt();" class="btn_black btn_right">저장</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

