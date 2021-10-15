<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
	//datepicker
	goDatepickerDraw();
	
	// 회원사 거절
	$("#newUserApplyImprove").on("click", function(){
		if(confirm("회원사 승인거절을 하시겠습니까?")){
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
		if(confirm("회원사 확인완료를 하시겠습니까?")){
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
			<h2>모집인 등록신청 확인 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
	
	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/newUser/newUserApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="plStat" id="plStat" value=""/>
		
		<div class="contents">
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
						<td colspan="3">${result.userRegInfo.plRegStatNm } <a href="javascript:void(0);" class="btn_Lgray btn_small mgl5" onclick="goUserStepHistoryShow('${result.userRegInfo.masterSeq }');">이력보기</a></td>
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
						<th>금융상품유형</th>
						<td colspan="3">${result.userRegInfo.plProductNm }</td>
					</tr>
					<tr>
						<th>업종</th>
						<td colspan="3">${result.userRegInfo.plWork }</td>
					</tr>
					<tr>
						<th>상호</th>
						<td>${result.userRegInfo.plMerchantName }</td>
						<th>대표이사</th>
						<td>${result.userRegInfo.plCeoName }</td>
					</tr>
					<tr>
						<th>대표이사 주민번호</th>
						<td>${result.userRegInfo.plMZId }</td>
						<th>대표이사 휴대폰번호</th>
						<td>${result.userRegInfo.plCellphone }</td>
					</tr>
					<tr>
						<th>법인등록번호</th>
						<td>${result.userRegInfo.plMerchantNo }</td>
						<th>설립년월일</th>
						<td>${result.userRegInfo.corpFoundDate }</td>
					</tr>
					<tr>
						<th>본점소재지</th>
						<td>${result.userRegInfo.addrBase}</td>
						<th>상세주소(법인등기부등본상)</th>
						<td>${result.userRegInfo.addrDetail }</td>
					</tr>
					<tr>
						<th>자본금(백만원)</th>
						<td colspan="3">${result.userRegInfo.capital }</td>
					</tr>
					<tr>
						<th>의결권있는 발행주식 총수</th>
						<td colspan="3">${result.userRegInfo.votingStockCnt }</td>
					</tr>
					<tr>
						<th>영위하는 다른 업종</th>
						<td colspan="3">${result.userRegInfo.otherField }</td>
					</tr>
					<tr>
						<th>관할검찰청 또는 지청</th>
						<td colspan="3">${result.userRegInfo.withinGovr }</td>
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
	
			<h3>신청인 관련 서류</h3>
			<div id="table05">
				<table class="view_table">
					<colgroup>
						<col width="38%"/>
						<col width="62%"/>
					</colgroup>
					<tr>
						<th class="acenter">정관 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType1.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="1"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType1.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">법인등기부등본 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType2.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="2"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType2.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">설립,등록 신청의 의사록 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType3.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="3"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType3.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">본점 위치 및 명칭을 기재한 서류</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType4.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="4"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType4.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">주주명부 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType5.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="5"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType5.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">영위하는 다른 업종에 대한 증빙서류</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType6.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="6"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType6.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">업무수행기준 *</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType15.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="Y" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="15"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType15.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">위탁계약서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType31.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="31"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType31.fileSeq }">다운로드</a>
						</td>
					</tr>
					<tr>
						<th class="acenter">위탁 금융상품직접판매업자 확인서</th>
						<td>
							<input type="text" class="w50 file_input" value="${result.userRegInfo.fileType32.fileFullNm }" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }" readonly disabled>
							<input type="file" name="files" class="inputFile" data-essential="N" style="display: none;"/>
							<input type="hidden" name="fileTypeList" value="32"/>
							<a href="javascript:void(0);" class="btn_black btn_small mgl5 goFileDownload" data-fileSeq="${result.userRegInfo.fileType32.fileSeq }">다운로드</a>
						</td>
					</tr>
				</table>
			</div>
	
			<div class="btn_wrap">
				<c:if test="${result.userRegInfo.plRegStat eq '1' and result.userRegInfo.plStat eq '2'}">
					<a href="javascript:void(0);" class="btn_Lgray btn_right_small02 w100p" id="newUserApplyImprove">거절</a>
					<a href="javascript:void(0);" class="btn_blue btn_right_small01 w100p" id="newUserApply">확인</a>
				</c:if>
				<a href="javascript:void(0);" class="btn_gray" onclick="goUserRegInfoList();">목록</a>
			</div>
		</div>
	</form>
</div>

