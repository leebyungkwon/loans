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
function goCorpImwonInfoDel(excSeq) {
	if(confirm("정말 삭제하시겠습니까?")){
		var p = {
			  url		: "/member/user/deleteUserRegCorpImwonInfo"	
			, param		: {
				 masterSeq 	: "${result.userRegInfo.masterSeq }"
				,excSeq 	: excSeq  
			}
			, success 	: function (opt,result) {
				goTab2("2");
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
			<li class="on"><a href="javascript:void(0);" onclick="goTab2('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('3');">전문성 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab2('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab2('5');">기타 첨부할 서류</a></li>
		</ul>
	</div>
		
	<div class="contents">
		<c:choose>
			<c:when test="${fn:length(result.imwonList) > 0 }">
				<c:forEach var="corpImwonList" items="${result.imwonList }" varStatus="status">
					<div class="data_wrap">
						<h3>기본정보</h3>
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
										<th>신규경력 구분</th>
										<td colspan="3">${corpImwonList.careerTypNm }</td>
									</tr>
									<tr>
										<th>이름</th>
										<td>${corpImwonList.excName }</td>
										<th>주민번호</th>
										<td>${corpImwonList.plMZId }</td>
									</tr>
									<tr>
										<th>직위</th>
										<td>${corpImwonList.positionNm }</td>
										<th>금융상품유형</th>
										<td>${result.userRegInfo.plProductNm }</td>
									</tr>
									<tr>
										<th>교육이수번호</th>
										<td colspan="3">${corpImwonList.plEduNo }</td>
									</tr>
									<tr>
										<th>경력시작일</th>
										<td>${corpImwonList.careerStartDate }</td>
										<th>경력종료일</th>
										<td>${corpImwonList.careerEndDate }</td>
									</tr>
									<tr>
										<th>상근여부</th>
										<td>${corpImwonList.fullTmStatNm }</td>
										<th>전문인력여부</th>
										<td>${corpImwonList.expertStatNm }</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<h3>1. 대표 및 임원관련 서류</h3>
						<div id="table06">
							<table class="view_table">
								<colgroup>
									<col width="38%">
									<col width="62%">
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">대표자 이력서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType7 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType7.fileSeq }">${corpImwonList.fileType7.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<th class="acenter">대표자 경력증명서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType8 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType8.fileSeq }">${corpImwonList.fileType8.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<%-- 
									<tr>
										<th class="acenter">임원자격에 적합함에 관한 확인서(결격사유없음 확인서) 및 증빙서류 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType9 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType9.fileSeq }">${corpImwonList.fileType9.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									 --%>
									<tr>
										<th class="acenter">인감증명서</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType10 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType10.fileSeq }">${corpImwonList.fileType10.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<%-- 
									<tr>
										<th class="acenter">후견부존재증명서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType27 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType27.fileSeq }">${corpImwonList.fileType27.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									 --%>
								</tbody>
							</table>
						</div>
						
						<h3>2. 금융상품 관련 서류</h3>
						<div id="table07">
							<table class="view_table">
								<colgroup>
									<col width="38%">
									<col width="62%">
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">위탁계약서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType11 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType11.fileSeq }">${corpImwonList.fileType11.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<th class="acenter">금융상품 유형 등 위탁내용에 대한 확인서<br>(계약서가 없거나,계약서 상에 금융상품에 대한 내용이 없는 경우)</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType28 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType28.fileSeq }">${corpImwonList.fileType28.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<h3>3. 교육이수관련 서류</h3>
						<div id="table08">
							<table class="view_table">
								<colgroup>
									<col width="38%">
									<col width="62%">
								</colgroup>
								<tbody>
									<c:if test="${corpImwonList.careerTyp eq '1' }">
										<tr>
											<th class="acenter">대표 인증서(신규) *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType13 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType13.fileSeq }">${corpImwonList.fileType13.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<c:if test="${corpImwonList.careerTyp eq '2' }">
										<tr>
											<th class="acenter">대표 경력교육과정 수료증 *</th>
											<td>
												<c:choose>
													<c:when test="${corpImwonList.fileType12 ne null }">
														<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType12.fileSeq }">${corpImwonList.fileType12.fileFullNm }</a>
													</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<tr>
										<th class="acenter">대표 경력증명서 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType14 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType14.fileSeq }">${corpImwonList.fileType14.fileFullNm }</a>
												</c:when>
												<c:otherwise>-</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<h3>4. 업무수행기준요건관련 서류</h3>
						<div id="table09">
							<table class="view_table">
								<colgroup>
									<col width="38%">
									<col width="62%">
								</colgroup>
								<tbody>
									<tr>
										<th class="acenter">업무수행기준요건관련 서류 *</th>
										<td>
											<c:choose>
												<c:when test="${corpImwonList.fileType15 ne null }">
													<a href="javascript:void(0);" class="goFileDownload" data-fileSeq="${corpImwonList.fileType15.fileSeq }">${corpImwonList.fileType15.fileFullNm }</a>
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
								<a href="javascript:void(0);" class="btn_Lgray btn_middle" onclick="goCorpImwonInfoDel('${corpImwonList.excSeq }');">삭제</a>
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
									<td colspan="3" style="text-align: center; font-weight: bold;">등록된 대표자 및 임원관련 사항이 존재하지 않습니다.</td>
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

