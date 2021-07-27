<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/userReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	//변경요청상태이면 삭제 불가
	var plStat = "${result.userRegInfo.plStat}";
	if(plStat == "3"){
		$(".btn_wrap02").remove();
		//$(".data_wrap").addClass("mgt30");
	}
}

//삭제 -> 일단 delete로(2021.05.12)
function goCorpExpertInfoDel(expSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegCorpExpertInfo"	
			, param		: {
				 masterSeq 	: "${result.userRegInfo.masterSeq }"
				,expSeq 	: expSeq  
			}
			, success 	: function (opt,result) {
				goTab2("3");
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
			<h2>모집인 조회 및 변경 - 법인</h2>
		</div>
	</div>

	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li class="on"><a href="javascript:void(0);" onclick="goTab2('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>

	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.expertList) > 0 }">
				<c:forEach var="corpExpertList" items="${result.expertList }" varStatus="status">
					<div class="data_wrap">
						<h3>기본정보</h3>
						<div id="table03">
							<table class="view_table">
								<colgroup>
									<col width="15%">
									<col width="35%">
									<col width="15%">
									<col width="35%">
								</colgroup>
								<tbody>
									<tr>
										<th>신규경력 구분</th>
										<td colspan="3">${corpExpertList.careerTypNm }</td>
									</tr>
									<tr>
										<th>이름</th>
										<td>${corpExpertList.expName }</td>
										<th>주민번호</th>
										<td>${corpExpertList.plMZId }</td>
									</tr>
									<tr>
										<th>금융상품유형</th>
										<td colspan="3">${result.userRegInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호/인증서번호</th>
										<td colspan="3">${corpExpertList.plEduNo }</td>
									</tr>
									<tr>
										<th>경력시작일</th>
										<td>${corpExpertList.careerStartDate }</td>
										<th>경력종료일</th>
										<td>${corpExpertList.careerEndDate }</td>
									</tr>
								</tbody>
							</table>
						</div>
				
						<h3>전문인력관련 서류</h3>
						<div id="table10">
							<table class="view_table">
								<colgroup>
									<col width="38%"/>
									<col width="62%"/>
								</colgroup>
								<tbody>
									<c:if test="${corpExpertList.careerTyp eq '1' }">
										<tr>
											<th class="acenter">인증서(신규) *</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType17 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType17.fileSeq }">${corpExpertList.fileType17.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<c:if test="${corpExpertList.careerTyp eq '2' }">
										<tr>
											<th class="acenter">경력교육과정 수료증 *</th>
											<td>
												<c:choose>
													<c:when test="${corpExpertList.fileType16 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType16.fileSeq }">${corpExpertList.fileType16.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<tr>
										<th class="acenter">경력증명서</th>
										<td>
											<c:choose>
												<c:when test="${corpExpertList.fileType18 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpExpertList.fileType18.fileSeq }">${corpExpertList.fileType18.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<div class="btn_wrap02">
							<div class="right">
								<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpExpertInfoDel('${corpExpertList.expSeq }');">삭제</a>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="data_wrap">
					<div id="table02">
						<table class="view_table">
							<colgroup>
								<col width="15%">
								<col width="35%">
								<col width="15%">
								<col width="35%">
							</colgroup>
							<tbody>
								<tr>
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 전문인력이 존재하지 않습니다.</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="btn_wrap" id="target">
			<a href="javascript:void(0);" class="btn_gray" onclick="goUserConfirmList();">목록</a>
		</div>
	</div>
</div>

