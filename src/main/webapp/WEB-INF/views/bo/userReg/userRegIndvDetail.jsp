<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
function pageLoad(){
	//파일찾기
	$(".goFileUpload").on("click", function () {
		$(this).prev().prev().click();
	});
	
	//첨부파일명 보여주기
	$(".inputFile").on("change", function () {
		var fileVal 	= $(this).val().split("\\");
		var fileName 	= fileVal[fileVal.length - 1];
		$(this).prev().val(fileName);
	});
	
	//첨부파일 삭제
	$(".goFileDel").on("click", function () {
		var fileSeq 	= $(this).attr("data-fileSeq");
		var fileType 	= $(this).attr("data-fileType");
		var essential 	= $(this).attr("data-essential");
		var targetArea 	= $(this).parent();
		
		if(confirm("삭제하시겠습니까?")){
			var p = {
				  url		: "/common/fileDelete"
				, param		: {
					fileSeq : fileSeq
				}
				, success	: function(opt,result) {
					if(result.data > 0){
						alert("삭제되었습니다.");
						
						var html = '';
						
						html += '<input type="text" class="w50 file_input" readonly disabled>';
						html += '<input type="file" name="files" class="inputFile" data-essential="'+essential+'" style="display: none;"/> ';
						html += '<input type="hidden" name="fileTypeList" value="'+fileType+'"/>';
						html += '<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>';

						targetArea.empty();
						targetArea.html(html);
					}
				}
			}
			AjaxUtil.post(p);
		}
	});
	
	//첨부파일 다운로드
	$(".goFileDownload").on("click", function () {
		var fileSeq 	= $(this).attr("data-fileSeq");
		
		var p = {
			  url 			: "/common/fileDown"
			, contType		: "application/json; charset=UTF-8"
			, responseType	: "arraybuffer"
			, param 		: {
				fileSeq : fileSeq
			}
		}
		AjaxUtil.post(p);
	});
	
}

//수정
function goUserRegInfoUpdt() {
	//validation
	var vdChkResult = 0;
	
	$(".inputFile").each(function(){
		if($(this).attr("data-essential") == "Y" && $(this).val() == ""){
			vdChkResult++;
		}
	});
	if(vdChkResult > 0){
		alert("필수 첨부서류가 누락되었습니다.");
		return;
	}
	//수정
	var p = {
		  name 		: "userRegInfoUpdFrm"
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("저장되었습니다.");
				location.reload();
			}
 	    }
	}
	AjaxUtil.files(p);
}

//모집인 등록 삭제(=취소) -> 등록신청취소건으로 은행연합회에 공유되고 해당 내용은 삭제되야 한다.
function goUserRegInfoCancel() {
	
	var p = {
		  url		: "/member/user/updatePlRegStat"	
		, param		: {
			 masterSeq 	: $("#masterSeq").val()
			,plStat		: '6'
		}
		, success 	: function (opt,result) {
			if(result.data > 0){
				alert("취소되었습니다.");
				location.reload();
			}
	    }
	}
	AjaxUtil.post(p);
}
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록 - 개인</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/user/updateUserRegInfo" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="fileGrpSeq" value="${result.userRegInfo.fileSeq }"/>
		
		<div class="contents">
			<h3>등록정보</h3>
			<div id="table">
				<table class="view_table">
					<tr>
						<th>회원사</th>
						<td>${result.userRegInfo.comCodeNm }</td>
						<th>담당자</th>
						<td>${result.userRegInfo.memberNm } (<a href="${result.userRegInfo.email }">${result.userRegInfo.email }</a>, ${result.userRegInfo.mobileNo })</td>
					</tr>
					<tr>
						<th>모집인 상태</th>
						<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5">이력보기</a></td>
						<th>결제여부</th>
						<td>${result.userRegInfo.plPayStat } (국민카드 / 2021.10.20)</td>
					</tr>
					<tr>
						<th>처리상태</th>
						<td colspan="3">${result.userRegInfo.plStatNm }</td>
					</tr>
					<tr>
						<th>모집인 분류</th>
						<td colspan="3">${result.userRegInfo.plClassNm }</td>
					</tr>
					<tr>
						<th>신규경력구분</th>
						<td colspan="3">${result.userRegInfo.careerTypNm }</td>
					</tr>
					<tr>
						<th>금융상품유형</th>
						<td colspan="3">${result.userRegInfo.plProductNm }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td><input type="text" name="plMName" class="w100" value="${result.userRegInfo.plMName }" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"이름을 입력해 주세요."}'></td>
						<th>주민번호</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.plProduct eq '1' }">
									<!-- 금융상품유형이 대출인 경우에는 수정 불가 -->
									${result.userRegInfo.plMZId }
								</c:when>
								<c:otherwise>
									<input type="text" name="plMZId" class="w100" value="${result.userRegInfo.plMZId }" maxlength="20" data-vd='{"type":"text","len":"1,20","req":true,"msg":"주민등록번호를 입력해 주세요."}'>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3"><input type="text" name="plCellphone" class="w100" value="${result.userRegInfo.plCellphone }" maxlength="13" data-vd='{"type":"mobileNo","len":"1,13","req":true,"msg":"휴대폰번호를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>주소</th>
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
							<input type="text" name="addrDetail" class="w60" value="${result.userRegInfo.addrDetail }">
						</td>
					</tr>
					<tr>
						<th>교육이수번호</th>
						<td colspan="3"><input type="text" name="plEduNo" class="w100" value="${result.userRegInfo.plEduNo }"></td>
					</tr>
					<tr>
						<th>경력시작일</th>
						<td><input type="text" name="careerStartDate" class="w100" value="${result.userRegInfo.careerStartDate }"></td>
						<th>경력종료일</th>
						<td><input type="text" name="careerEndDate" class="w100" value="${result.userRegInfo.careerEndDate }"></td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td colspan="3"><input type="text" name="comContDate" class="w50" value="${result.userRegInfo.comContDate }"></td>
					</tr>
					<tr>
						<th>위탁예정기간</th>
						<td colspan="3"><input type="text" name="entrustDate" class="w50" value="${result.userRegInfo.entrustDate }"></td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '4' }">
						<tr>
							<th>반려사유</th>
							<td colspan="3">${result.userRegInfo.sendMsg }</td>
						</tr>
					</c:if>
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
						<th class="acenter">주민등록증 사진 (등록증 게시용) *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType1 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType1.fileSeq }">${result.fileType1.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType1.fileSeq }" data-fileType="1" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="1"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType2 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType2.fileSeq }">${result.fileType2.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType2.fileSeq }" data-fileType="2" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="2"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">교육과정 이수확인서 (경력) 또는 인증서(신규) *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType3 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType3.fileSeq }">${result.fileType3.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType3.fileSeq }" data-fileType="3" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="3"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">경력증명서 *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType4 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType4.fileSeq }">${result.fileType4.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType4.fileSeq }" data-fileType="4" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="4"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType5 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType5.fileSeq }">${result.fileType5.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType5.fileSeq }" data-fileType="5" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="5"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType6 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType6.fileSeq }">${result.fileType6.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType6.fileSeq }" data-fileType="6" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="6"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">대리인 신청 위임장(위임인 인간날인) 및 위임인 인감증명서</th>
						<td>
							<c:choose>
								<c:when test="${result.fileType7 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.fileType7.fileSeq }">${result.fileType7.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.fileType7.fileSeq }" data-fileType="7" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="7"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_wrap">
				<a href="/member/user/userRegPage" class="btn_gray">목록</a>
				<c:if test="${result.userRegInfo.plStat ne '2' }"> 
					<!-- 승인요청상태가 아닐 때만 수정/삭제 가능 -->
					<a href="javascript:void(0);" class="btn_black btn_right02" onclick="goUserRegInfoUpdt();">저장</a>
					<a href="javascript:void(0);" class="btn_black btn_right" onclick="goUserRegInfoCancel();">삭제</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

