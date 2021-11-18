<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript" src="/static/js/newUserReg/common.js"></script>

<script type="text/javascript">
function pageLoad(){
	
	//datepicker
	goDatepickerDraw();
	
	// 금융회사 거절
	$("#newUserApplyImprove").on("click", function(){
		if(confirm("등록신청 거절을 하시겠습니까?")){
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
	
	
	// 금융회사 승인
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

<!-- 	<div class="tap_wrap" style="margin-bottom: 30px;">
		<ul>
			<li class="on"><a href="javascript:void(0);" class="single" onclick="goTab('1');">등록정보</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('2');">대표자 및 임원관련<br />사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('3');">업무수행인력<br />관련 사항</a></li>
			<li><a href="javascript:void(0);" onclick="goTab('4');">전산설비 관리 인력에<br />관한 사항</a></li>
			<li><a href="javascript:void(0);" class="single" onclick="goTab('5');">기타 첨부할 서류</a></li>
		</ul>
	</div> -->
	
	<form name="userRegInfoUpdFrm" id="userRegInfoUpdFrm" action="/member/newUser/newUserApply" method="post" enctype="multipart/form-data">
		<input type="hidden" name="masterSeq" id="masterSeq" value="${result.userRegInfo.masterSeq }"/>
		<input type="hidden" name="plStat" id="plStat" value=""/>
		
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<colgroup>
						<col width="15%">
						<col width="35%">
						<col width="15%">
						<col width="35%">
					</colgroup>
					<tbody>
						<tr>
							<th>모집인유형</th>
							<td>${result.userRegInfo.plClassNm }</td>
							<th>법인명</th>
							<td>${result.userRegInfo.plMerchantName }</td>
						</tr>
						<tr>
							<th>승인요청일</th>
							<td>${result.userRegInfo.comRegDate }</td>
							<th>법인번호</th>
							<td>${result.userRegInfo.plMerchantNo }</td>
						</tr>
						<tr>
							<th>대표자명</th>
							<td>${result.userRegInfo.plCeoName }</td>
							<th>휴대폰번호</th>
							<td>${result.userRegInfo.plCellphone }</td>
						</tr>
						<tr>
							<th>금융상품유형</th>
							<td>${result.userRegInfo.plProductNm }</td>
							<th>금융상품 세부내용</th>
							<td>
								<c:choose>
									<c:when test="${fn:length(result.plProductDetailList) > 0 }">
										<c:forEach var="productDetailList" items="${result.plProductDetailList }" varStatus="loop">
											${productDetailList.plProductDtlCdNm}
											<c:if test="${!loop.last}"> / </c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										해당없음
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th>계약체결(예정)일자</th>
							<td>${result.userRegInfo.comContDate }</td>
							<th>계약기간</th>
							<td>${result.userRegInfo.entrustDate }</td>
						</tr>
					</tbody>
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

