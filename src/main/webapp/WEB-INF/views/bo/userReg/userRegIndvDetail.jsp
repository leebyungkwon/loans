<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//승인요청상태이면 수정 불가
	var plStat = "${result.userRegInfo.plStat}";
	if(plStat == "2"){
		$("input").prop("readonly",true);
		$("option").attr("disabled",true);
		$(".goFileDel").remove();
		$(".inputFile").each(function(){
			$(this).parent("td").html("-");
		});
	}
	
	//datepicker
	$("#date_cal01").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#careerStartDate").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal02").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#careerEndDate").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal03").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#comContDate").val(dateText1);
			$(this).hide();
		}
	});
	$("#date_cal04").datepicker({
		 dateFormat	: "yy-mm-dd"
		,onSelect	: function(dateText1,inst) {
			$("#entrustDate").val(dateText1);
			$(this).hide();
		}
	});
}

$(document).mouseup(function(e){
	var calendar01 = $(".calendar01");
	if(calendar01.has(e.target).length === 0){
		calendar01.hide();
	}
});

//수정
function goUserRegInfoUpdt() {
	//수정
	if(confirm("저장하시겠습니까?")){
		goFileTypeListDisabled();
		
		var p = {
			  name 		: "userRegInfoUpdFrm"
			, success 	: function (opt,result) {
				location.reload();
	 	    }
		}
		AjaxUtil.files(p);
	}
}

//모집인 등록 삭제(=취소)
function goUserRegInfoCancel() {
	if(confirm("삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegInfo"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '6'
			}
			, success 	: function (opt,result) {
				if(result.data > 0){
					alert("삭제되었습니다.");
					goUserRegInfoList();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}
</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

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
						<td>${result.userRegInfo.plRegStatNm }</td>
						<th>결제여부</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.plPayStat ne null }">
									${result.userRegInfo.plPayStat } (국민카드 / 2021.10.20)
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
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
						<th>법인사용인여부</th>
						<td colspan="3">${result.userRegInfo.corpUserYn }</td>
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
						<td><input type="text" name="plMZId" class="w100" value="${result.userRegInfo.plMZId }" maxlength="14" placeholder="- 포함" data-vd='{"type":"text","len":"14,14","req":true,"msg":"주민등록번호(- 포함)를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3"><input type="text" name="plCellphone" class="w100" value="${result.userRegInfo.plCellphone }" maxlength="13" placeholder="- 포함" data-vd='{"type":"mobileNo","len":"1,13","req":true,"msg":"휴대폰번호(- 포함)를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>주소</th>
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
							<input type="text" name="addrDetail" class="w60" value="${result.userRegInfo.addrDetail }" maxlength="200">
						</td>
					</tr>
					<tr>
						<th>교육이수번호</th>
						<td colspan="3"><input type="text" name="plEduNo" class="w100" value="${result.userRegInfo.plEduNo }" maxlength="10" data-vd='{"type":"text","len":"10,10","req":true,"msg":"교육이수번호를 입력해 주세요."}'></td>
					</tr>
					<tr>
						<th>경력시작일</th>
						<td>
							<input type="text" name="careerStartDate" id="careerStartDate" onclick="$('#date_cal01').show();" class="w100" value="${result.userRegInfo.careerStartDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력시작일(- 포함)을 입력해 주세요."}' readonly="readonly">
							<!-- <a class="calendar_ico" onclick="$('#date_cal01').show();"></a> -->
						 	<div id="date_cal01" class="calendar01"></div>
						</td>
						<th>경력종료일</th>
						<td>
							<input type="text" name="careerEndDate" id="careerEndDate" onclick="$('#date_cal02').show();" class="w100" value="${result.userRegInfo.careerEndDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"경력종료일(- 포함)을 입력해 주세요."}' readonly="readonly">
							<!-- <a class="calendar_ico" onclick="$('#date_cal02').show();"></a> -->
						 	<div id="date_cal02" class="calendar01"></div>
						</td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td>
							<input type="text" name="comContDate" id="comContDate" onclick="$('#date_cal03').show();" class="w100" value="${result.userRegInfo.comContDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"계약일자(- 포함)를 입력해 주세요."}' readonly="readonly">
							<!-- <a class="calendar_ico" onclick="$('#date_cal03').show();"></a> -->
						 	<div id="date_cal03" class="calendar01"></div>
						</td>
						<th>위탁예정기간</th>
						<td>
							<input type="text" name="entrustDate" id="entrustDate" onclick="$('#date_cal04').show();" class="w100" value="${result.userRegInfo.entrustDate }" maxlength="10" placeholder="- 포함" data-vd='{"type":"text","len":"10,10","req":true,"msg":"위탁예정기간(- 포함)을 입력해 주세요."}' readonly="readonly">
							<!-- <a class="calendar_ico" onclick="$('#date_cal04').show();"></a> -->
						 	<div id="date_cal04" class="calendar01"></div>
						</td>
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
						<th class="acenter">사진 (등록증 게시용) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType1 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">${result.userRegInfo.fileType1.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" data-fileType="1" data-essential="Y">삭제</a>
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
								<c:when test="${result.userRegInfo.fileType2 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">${result.userRegInfo.fileType2.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" data-fileType="2" data-essential="Y">삭제</a>
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
						<th class="acenter">교육과정 이수확인서 (경력)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType3 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">${result.userRegInfo.fileType3.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" data-fileType="3" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="3"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="3" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">인증서(신규)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType4 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">${result.userRegInfo.fileType4.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" data-fileType="4" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="4"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="4" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">경력증명서 *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType5 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">${result.userRegInfo.fileType5.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" data-fileType="5" data-essential="Y">삭제</a>
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
						<th class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType6 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">${result.userRegInfo.fileType6.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" data-fileType="6" data-essential="Y">삭제</a>
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
						<th class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType7 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }">${result.userRegInfo.fileType7.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType7.fileSeq }" data-fileType="7" data-essential="Y">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="7"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">대리인 신청 위임장(위임인 인간날인)</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType8 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }">${result.userRegInfo.fileType8.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType8.fileSeq }" data-fileType="8" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="8"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="8" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th class="acenter">위임인 인감증명서</th>
						<td>
							<c:choose>
								<c:when test="${result.userRegInfo.fileType9 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }">${result.userRegInfo.fileType9.fileFullNm }</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl10 goFileDel" data-fileSeq="${result.userRegInfo.fileType9.fileSeq }" data-fileType="9" data-essential="N">삭제</a>
								</c:when>
								<c:otherwise>
									<input type="text" class="w50 file_input" readonly disabled>
									<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
									<input type="hidden" name="fileTypeList" value="9"/>
									<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileUpload">파일찾기</a>
									<a href="javascript:void(0);" class="btn_gray btn_del mgl5 goFileReset" data-fileType="9" data-essential="N">초기화</a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_wrap">
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
				<c:if test="${result.userRegInfo.plStat ne '2' }"> 
					<!-- 승인요청상태가 아닐 때만 수정/삭제 가능 -->
					<a href="javascript:void(0);" class="btn_blue btn_right02" onclick="goUserRegInfoUpdt();">저장</a>
					<a href="javascript:void(0);" class="btn_Lgray btn_right" onclick="goUserRegInfoCancel();">삭제</a>
				</c:if>
			</div>
		</div>
	</form>
</div>

