<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">

function pageLoad(){
	
	//datepicker
	goDatepickerDraw();
	
	
	// 회원사 거절
	$("#newUserApplyImprove").on("click", function(){
		if(confirm("회원사 거절을 하시겠습니까?")){
			$("#plStat").val("16");
			var p = {
				  name 		: "userRegInfoUpdFrm"
				, success 	: function (opt,result) {
					
					console.log("성공 #### ::" , result);
					
					if(result.data.code == "success"){
						alert(result.data.message);
						location.href="/member/newUser/newUserRegPage";
					}else{
						alert(result.data.message);
						location.reload();
					}
		 	    }
			}
			AjaxUtil.files(p);
		}
		
	});
	
	
	// 회원사 승인
	$("#newUserApply").on("click", function(){
		if(confirm("회원사 승인을 하시겠습니까?")){
			$("#plStat").val("15");
			var p = {
				  name 		: "userRegInfoUpdFrm"
				, success 	: function (opt,result) {
					
					console.log("성공 #### ::" , result);
					
					if(result.data.code == "success"){
						alert(result.data.message);
						location.href="/member/newUser/newUserRegPage";
					}else{
						alert(result.data.message);
						location.reload();
					}
		 	    }
			}
			AjaxUtil.files(p);
		}
	});
}


</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" value="${result.userRegInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 등록신청 확인 - 개인</h2>
		</div>
	</div>

	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/newUser/newUserApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="plStat" id="plStat" value=""/>
		
		<div class="contents">
			<h3>등록정보</h3>
			<div id="table">
				<table class="view_table">
					<tr>
						<th>회원사</th>
						<td>${result.userRegInfo.comCodeNm }</td>
						<th>담당자</th>
						<td>${result.userRegInfo.memberNm } (${result.userRegInfo.email }<c:if test="${result.userRegInfo.extensionNo ne null && result.userRegInfo.extensionNo ne '' }">, ${result.userRegInfo.extensionNo }</c:if>)</td>
					</tr>
					<tr>
						<th>가등록번호</th>
						<td colspan="3">${result.userRegInfo.preLcNum }</td>
					</tr>
					<tr>
						<th>모집인 상태</th>
						<td>${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
						<th>처리상태</th>
						<td>${result.userRegInfo.plStatNm }</td>
					</tr>
					<tr>
						<th>모집인 분류</th>
						<td colspan="3">${result.userRegInfo.plClassNm }</td>
					</tr>
					<tr>
						<th>법인사용인여부</th>
						<td colspan="3">${result.userRegInfo.corpUserYn }</td>
					</tr>
					<c:if test="${result.userRegInfo.corpUserYn eq 'Y' }">
						<tr>
							<th>법인명</th>
							<td>${result.userRegInfo.plMerchantName }</td>
							<th>법인등록번호</th>
							<td>${result.userRegInfo.plMerchantNo }</td>
						</tr>
					</c:if>
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
						<td>${result.userRegInfo.plMName }</td>
						<th>주민번호</th>
						<td>${result.userRegInfo.plMZId }</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td colspan="3">${result.userRegInfo.plCellphone }</td>
					</tr>
					
					<tr>
						<th>주소</th>
						<td>${result.userRegInfo.addrNm }</td>
						<th>상세주소</th>
						<td>${result.userRegInfo.addrDetail }</td>
					</tr>
					 
<%-- 					<tr>
						<th>주소</th>
						<td colspan="3">
							<select name="addr">
								<c:forEach var="addrCodeList" items="${result.addrCodeList }">
									<option value="${addrCodeList.codeDtlCd }" <c:if test="${addrCodeList.codeDtlCd eq result.userRegInfo.addr }">selected="selected"</c:if>>${addrCodeList.codeDtlNm }</option>
								</c:forEach>
							</select>
						</td>
					</tr> --%>
					<tr>
						<th>교육이수번호/인증서번호</th>
						<td colspan="3">${result.userRegInfo.plEduNo }</td>
					</tr>
					<tr>
						<th>경력시작일</th>
						<td>${result.userRegInfo.careerStartDate }</td>
						<th>경력종료일</th>
						<td>${result.userRegInfo.careerEndDate }</td>
					</tr>
					<tr>
						<th>계약일자</th>
						<td>${result.userRegInfo.comContDate }</td>
						<th>위탁예정기간</th>
						<td>${result.userRegInfo.entrustDate }</td>
					</tr>
					<c:if test="${result.userRegInfo.plStat eq '5' || result.userRegInfo.plStat eq '6' || result.userRegInfo.plStat eq '7' }">
						<tr>
							<th>보완요청사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<c:if test="${result.userRegInfo.plStat eq '10' || result.userRegInfo.plStat eq '11' || result.userRegInfo.plStat eq '12' }">
						<tr>
							<th>사유</th>
							<td colspan="3">${result.userRegInfo.plHistTxt }</td>
						</tr>
					</c:if>
					<tr>
						<th>승인요청사유</th>
						<td colspan="3">
							<textarea rows="6" cols="" id="applyHistTxt" name="applyHistTxt" class="w100">${result.userRegInfo.applyHistTxt }</textarea>
						</td>
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
						<th class="acenter">사진</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType1.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">신분증 사본 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType2.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="2"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">다운로드</a>
						</td>
					</tr>
					<c:if test="${result.userRegInfo.careerTyp eq '1' }">
						<tr>
							<th class="acenter">인증서 *</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType4.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="4"/>
								<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">다운로드</a>
							</td>
						</tr>
					</c:if>
					<c:if test="${result.userRegInfo.careerTyp eq '2' }">
						<tr>
							<th class="acenter">교육수료증 *</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType3.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="3"/>
								<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">다운로드</a>
							</td>
						</tr>
						<tr>
							<th class="acenter">경력증명서</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType5.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="5"/>
								<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">다운로드</a>
							</td>
						</tr>
						<tr>
							<th class="acenter">위탁계약서</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType6.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="6"/>
								<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">다운로드</a>
							</td>
						</tr>
						<tr>
							<th class="acenter">위탁 금융상품직접판매업자 확인서</th>
							<td>
								<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType12.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }" readonly disabled>
								<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
								<input type="hidden" name="fileTypeList" value="12"/>
								<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType12.fileSeq }">다운로드</a>
							</td>
						</tr>
					</c:if>
				</table>
			</div>
			
			<div class="btn_wrap">
				<c:if test="${result.userRegInfo.plRegStat eq '1' and result.userRegInfo.plStat eq '2'}">
					<a href="javascript:void(0);" class="btn_Lgray btn_right_small02 w100p" id="newUserApplyImprove">회원사 거절</a>
					<a href="javascript:void(0);" class="btn_blue btn_right_small01 w100p" id="newUserApply">회원사 승인</a>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
			</div>
		</div>
	</form>
</div>

