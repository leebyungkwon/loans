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
	}else if(num == "3"){
		plStat = "7";
		plRegStat = "3";
	}else{
		alert("오류가 발생하였습니다.");
		return false;
	}
	
	if(confirm("요청사항을 승인하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 		: $("#masterSeq").val()
				,plStat			: plStat
				,plRegStat		: plRegStat
				,ordPlStat		: $("#oldPlStat").val()
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage";
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
function goRecruitImprove(){
	if(WebUtil.isNull($("#plHistTxt").val())){
		alert("사유를 입력해 주세요");
		$("#plHistTxt").focus();
		return false;
	}
	
	if(confirm("보완요청을 하시겠습니까?")){
		var p = {
			  url		: "/admin/recruit/updatePlStat"	
			, param		: {
				 masterSeq 	: $("#masterSeq").val()
				,plStat		: '5'
				,plHistTxt	: $("#plHistTxt").val()
				,ordPlStat	: $("#oldPlStat").val()
			}
			, success 	: function (opt,result) {
				if(result.data.code == "success"){
					alert(result.data.message);
					location.href="/admin/recruit/recruitPage";
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
	<input type="hidden" name="masterSeq" id="masterSeq" value="${result.recruitInfo.masterSeq }"/>
	<input type="hidden" name="oldPlStat" id="oldPlStat" value="${result.recruitInfo.plStat }"/>
</form>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>모집인 조회 및 변경 - 개인</h2>
		</div>
	</div>

	<div class="contents">
		<h3>등록정보</h3>
		<div id="table">
			<table class="view_table">
				<tr>
					<th>회원사</th>
					<td>${result.recruitInfo.comCodeNm }</td>
					<th>담당자</th>
					<td>${result.recruitInfo.memberNm } (<a href="${result.recruitInfo.email }">${result.recruitInfo.email }</a>, ${result.recruitInfo.mobileNo })</td>
				</tr>
				<tr>
					<th>모집인 상태</th>
					<td>${result.recruitInfo.plRegStatNm } <a href="javascript:alert('준비중입니다.');" class="btn_Lgray btn_small mgl5">이력보기</a></td>
					<th>결제여부</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.plPayStat ne null }">
								${result.recruitInfo.plPayStat } (국민카드 / 2021.10.20)
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>처리상태</th>
					<td colspan="3">${result.recruitInfo.plStatNm }</td>
				</tr>
				<tr>
					<th>모집인 분류</th>
					<td colspan="3">${result.recruitInfo.plClassNm }</td>
				</tr>
				<tr>
					<th>신규경력구분</th>
					<td colspan="3">${result.recruitInfo.careerTypNm }</td>
				</tr>
				<tr>
					<th>금융상품유형</th>
					<td colspan="3">${result.recruitInfo.plProductNm }</td>
				</tr>
				<tr>
					<th>이름</th>
					<td>${result.recruitInfo.plMName }</td>
					<th>주민번호</th>
					<td>${result.recruitInfo.plMZId }</td>
				</tr>
				<tr>
					<th>휴대폰번호</th>
					<td colspan="3">${result.recruitInfo.plCellphone }</td>
				</tr>
				<tr>
					<th>주소</th>
					<td colspan="3">${result.recruitInfo.addrNm }</td>
				</tr>
				<tr>
					<th>교육이수번호 또는 인증번호</th>
					<td colspan="3">${result.recruitInfo.plEduNo }</td>
				</tr>
				<tr>
					<th>경력시작일</th>
					<td>${result.recruitInfo.careerStartDate }</td>
					<th>경력종료일</th>
					<td>${result.recruitInfo.careerEndDate }</td>
				</tr>
				<tr>
					<th>계약일자</th>
					<td colspan="3">${result.recruitInfo.comContDate }</td>
				</tr>
				<tr>
					<th>위탁예정기간</th>
					<td colspan="3">${result.recruitInfo.entrustDate }</td>
				</tr>
				
				<c:choose>
					<c:when test="${result.recruitInfo.plStat eq '3' or result.recruitInfo.plStat eq '7'
					or result.recruitInfo.plStat eq '2' or result.recruitInfo.plStat eq '5' or result.recruitInfo.plStat eq '7'}">
						<tr>
							<th>사유</th>
							<td colspan="3">
								<input type="text" id="plHistTxt" name="plHistTxt" class="w100" maxlength="200" value="${result.recruitInfo.plHistTxt }">
							</td>
						</tr>
					</c:when>
					<c:when test="${result.recruitInfo.plStat eq '4' or result.recruitInfo.plStat eq '7'}">
						<tr>
							<th>해지요청사유</th>
							<td colspan="3">${result.recruitInfo.plHistCd }</td>
						</tr>
						<tr>
							<th>해지일자</th>
							<td colspan="3">${result.recruitInfo.comHaejiDate }</td>
						</tr>
					</c:when>
				</c:choose>
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
							<c:when test="${result.recruitInfo.fileType1 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType1.fileSeq }">${result.recruitInfo.fileType1.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일 *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType2 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType2.fileSeq }">${result.recruitInfo.fileType2.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">교육과정 이수확인서 (경력)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType3 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType3.fileSeq }">${result.recruitInfo.fileType3.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">인증서(신규)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType4 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType4.fileSeq }">${result.recruitInfo.fileType4.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">경력증명서 *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType5 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType5.fileSeq }">${result.recruitInfo.fileType5.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">금융상품 유형, 내용에 대한 설명자료(계약서) *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType6 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType6.fileSeq }">${result.recruitInfo.fileType6.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">결격사유없음 확인서 (파산, 피한정후견인등) *</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType7 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType7.fileSeq }">${result.recruitInfo.fileType7.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">대리인 신청 위임장(위임인 인간날인)</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType8 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType8.fileSeq }">${result.recruitInfo.fileType8.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th class="acenter">위임인 인감증명서</th>
					<td>
						<c:choose>
							<c:when test="${result.recruitInfo.fileType9 ne null }">
								<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType9.fileSeq }">${result.recruitInfo.fileType9.fileFullNm }</a>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<c:if test="${result.recruitInfo.plStat eq '3' }">
					<tr>
						<th class="acenter">주민등록증 또는 주민등록 초본</th>
						<td>
							<c:choose>
								<c:when test="${result.recruitInfo.fileType10 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType10.fileSeq }">${result.recruitInfo.fileType10.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
					
					<tr>
						<th class="acenter">휴대폰 명의 확인서</th>
						<td>
							<c:choose>
								<c:when test="${result.recruitInfo.fileType11 ne null }">
									<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${result.recruitInfo.fileType11.fileSeq }">${result.recruitInfo.fileType11.fileFullNm }</a>
								</c:when>
								<c:otherwise>-</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_gray" onclick="goRecruitList();">목록</a>
			<c:if test="${result.recruitInfo.plStat eq '4'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(4);">해지승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>
			</c:if>
			<c:if test="${result.recruitInfo.plStat eq '2'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(2);">승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>
			</c:if>
			<c:if test="${result.recruitInfo.plStat eq '3'}">
				<a href="javascript:void(0);" class="btn_Lgray btn_right_small03 w100p" id="recruitApply" onclick="goRecruitApply(3);">변경승인</a>
				<a href="javascript:void(0);" class="btn_gray btn_right_small02 w100p" id="recruitImprove" onclick="goRecruitImprove();">보완요청</a>					
			</c:if>
		</div>
	</div>
</div>

