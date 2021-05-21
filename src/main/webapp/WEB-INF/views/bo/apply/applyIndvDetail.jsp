<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/recruit/common.js"></script>

<script type="text/javascript">
function pageLoad(){

}

//승인
function goRecruitApply(num){
	var plStat = "";
	var plRegStat = "";
	if(num == "2"){
		plStat = "7";
		plRegStat = "2";
	}else if(num == "4"){
		plStat = "7";
		plRegStat = "4";
	}else if(num == "6"){
		plStat = "7";
		plRegStat = "3";
	}else{
		alert("오류가 발생하였습니다.");
		return false;
	}
	
	if(confirm("요청사항을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/apply/updatePlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: '7'
				,plRegStat		: '2'
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/apply/applyPage";
				}else{
					alert(result.data.message);
					location.reload();
				}
		    }
		}
		AjaxUtil.post(p);
	}
}

//보완
function goApplyImprove(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("사유를 입력해 주세요");
		$("#plHistTxt").focus();
		return false;
	}
	
	if(confirm("보완요청을 하시겠습니까?")){
		var p = {
			  url		: "/admin/apply/updatePlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '5'
				,plHistTxt	: $("#plHistTxt").val()
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/apply/applyPage";
				}else{
					alert(result.data.message);
					location.reload();
				}
				
		    }
		}
		AjaxUtil.post(p);
	}
}

</script>

<form name="pageFrm" id="pageFrm" method="post">
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.applyInfo.masterSeq }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 개인</h2>
		</div>
	</div>

	<div class="contents">
		<div class="box">
			<h3>등록정보</h3>
			<div class="input_check_wrap right mgb0">
				<input type="checkbox" id="adminCheck" class="check" <c:if test="${result.applyInfo.chkYn eq 'Y'}">checked</c:if>>
				<label for="adminCheck">실무자 확인</label>
			</div>
		</div>
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.applyInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.applyInfo.memberNm } (<a href="${result.applyInfo.email }">${result.applyInfo.email }</a>, ${result.applyInfo.mobileNo })</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.applyInfo.plRegStatNm } <a href="javascript:alert('준비중입니다.');" class="btn_Lgray btn_small mgl5">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.plPayStat ne null }">
								${result.applyInfo.plPayStat } (국민카드 / 2021.10.20)
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>처리상태</th>
					<td colspan="3">${result.applyInfo.plStatNm }</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td colspan="3">${result.applyInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>신규경력구분</th>
					<td colspan="3">${result.applyInfo.careerTypNm }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.applyInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${result.applyInfo.plMName }</td>
					<th>주민번호</th>
					<td>${result.applyInfo.plMZId }</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td colspan="3">${result.applyInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>주소</th>
					<td colspan="3">${result.applyInfo.addrNm }</td>
				</tr>
				<tr>
					<th>교육이수번호 또는 인증번호</th>
					<td colspan="3">${result.applyInfo.plEduNo }</td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>${result.applyInfo.careerStartDate }</td>
					<th>경력종료일</th>
					<td>${result.applyInfo.careerEndDate }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td colspan="3">${result.applyInfo.comContDate }</td>
				</tr>
				<tr>
					<th>위탁예정기간</th>
					<td colspan="3">${result.applyInfo.entrustDate }</td>
				</tr>
				
				<c:choose>
					<c:when test="${result.applyInfo.plStat eq '3' or result.applyInfo.plStat eq '7'
					or result.applyInfo.plStat eq '2' or result.applyInfo.plStat eq '5' or result.applyInfo.plStat eq '7'}">
						<tr>
							<th>사유</th>
							<td colspan="3">
								<input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200" value="${result.applyInfo.plHistTxt }">
							</td>
						</tr>
					</c:when>
					<c:when test="${result.applyInfo.plStat eq '4' or result.applyInfo.plStat eq '7'}">
						<tr>
							<th>해지요청사유</th>
							<td colspan="3">${result.applyInfo.plHistCd }</td>
						</tr>
						<tr>
							<th>해지일자</th>
							<td colspan="3">${result.applyInfo.comHaejiDate }</td>
						</tr>
					</c:when>
				</c:choose>
			</table>
		</div>

		<h3>첨부서류</h3>
		<div id="table02">
			<table class="view_table border_table">
				<colgroup>
					<col width="50%"/>
					<col width="20%"/>
					<col width="30%"/>
				</colgroup>
				<tr>
					<th>구분</th>
					<th>첨부이미지</th>
					<th>체크사항</th>
				</tr>
				<tr>
					<td class="acenter">사진 (등록증 게시용) *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType1 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType1.fileSeq }">${result.applyInfo.fileType1.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd1" class="check check_cd" <c:if test="${!empty result.applyInfo.checkCd1}">checked</c:if>
							<c:if test="${empty result.applyInfo.fileType1.fileSeq}">disabled</c:if>
							 data-fileSeq="${result.applyInfo.fileType1.fileSeq }" >
							<label for="check_cd1">이미지 사이즈 확인</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType2.fileSeq }">${result.applyInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd2" class="check check_cd"
							<c:if test="${empty result.applyInfo.fileType2.fileSeq}">disabled</c:if> 
							<c:if test="${!empty result.applyInfo.checkCd2}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType2.fileSeq }" >
							<label for="check_cd2">기재내용 일치여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">교육과정 이수확인서 (경력)</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType3 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType3.fileSeq }">${result.applyInfo.fileType3.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd3" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType3.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd3}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType3.fileSeq }" >
							<label for="check_cd3">교육 이수 및 인증내역 검증</label> 
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">인증서(신규)</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType4 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType4.fileSeq }">${result.applyInfo.fileType4.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd4" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType4.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd4}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType4.fileSeq }" >
							<label for="check_cd4">교육 이수 및 인증내역 검증</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">경력증명서 *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType5 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType5.fileSeq }">${result.applyInfo.fileType5.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd5" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType5.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd5}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType5.fileSeq }" >
							<label for="check_cd5">경력 인정여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType6.fileSeq }">${result.applyInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd6" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType6.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd6}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType6.fileSeq }" >
							<label for="check_cd6">기재내용 일치 여부</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType7 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType7.fileSeq }">${result.applyInfo.fileType7.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd7" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType7.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd7}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType7.fileSeq }" >
							<label for="check_cd7">결격사유 유무 검증</label> 
						</div>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd8" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType7.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd8}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType7.fileSeq }" >
							<label for="check_cd8">서명 누락</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="acenter">대리인 신청 위임장(위임인 인간날인)</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType8 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType8.fileSeq }">${result.applyInfo.fileType8.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd9" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType9.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd9}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType9.fileSeq }" >
							<label for="check_cd9">인감 날인 여부</label>
						</div>											
					</td>
				</tr>
				<tr>
					<td class="acenter">위임인 인감증명서</td>
					<td>
						<c:choose>
							<c:when test="${result.applyInfo.fileType9 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType9.fileSeq }">${result.applyInfo.fileType9.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td>
						<div class="input_check_wrap mgr10">
							<input type="checkbox" id="check_cd10" class="check check_cd" 
							<c:if test="${empty result.applyInfo.fileType10.fileSeq}">disabled</c:if>
							<c:if test="${!empty result.applyInfo.checkCd10}">checked</c:if>
							 data-fileSeq="${result.applyInfo.fileType10.fileSeq }" >
							<label for="check_cd10">유효 증명서여부</label>
						</div>
					</td>
				</tr>
				<c:if test="${result.applyInfo.plStat eq '3' }">
					<tr>
						<td class="acenter">주민등록증 또는 주민등록 초본</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType10 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType10.fileSeq }">${result.applyInfo.fileType10.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
					
					<tr>
						<td class="acenter">휴대폰 명의 확인서</td>
						<td>
							<c:choose>
								<c:when test="${result.applyInfo.fileType11 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.applyInfo.fileType11.fileSeq }">${result.applyInfo.fileType11.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goApplyList();">목록</a>
			<c:if test="${result.applyInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(4);">해지승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '2'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>
			</c:if>
			<c:if test="${result.applyInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(3);">변경승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>					
			</c:if>
		</div>
	</div>
</div>

