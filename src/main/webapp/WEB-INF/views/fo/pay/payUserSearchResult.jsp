<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
function pageLoad() {
	//결제금액은 개인 2만원, 법인 20만원 ******
	//allat_approval.jsp의 결제금액 바꾸기
	if("${searchUserInfo.plClass }" == "1"){
		$("#allat_amt").val("20000");
		$("#testAmt").val("20000"); //결제 테스트
		
		var plName = "${searchUserInfo.plMName}";
		var resultPlName = "";
		if(plName.length > 10){
			resultPlName = plName.substr(0, 10);
			$("#allat_buyer_nm").val(resultPlName);
		}else{
			$("#allat_buyer_nm").val(plName);
		}
		
	}else{
		$("#allat_amt").val("200000");
		$("#testAmt").val("200000"); //결제 테스트
		
		var mName = "${searchUserInfo.plMerchantName}";
		var resultName = "";
		if(mName.length > 10){
			resultName = mName.substr(0, 10);
			$("#allat_buyer_nm").val(resultName);
		}else{
			$("#allat_buyer_nm").val(mName);
		}
		
	}
}

//결제 테스트
function goPayTest() {
	if(confirm("결제 후 환불은 불가능 합니다.\n계속 진행하시겠습니까?")){
		var params 	= $("#payTestFrm").serialize();
		var url 	= $("#payTestFrm").attr("action");
		
		var p = {
			 url		: url
			,param 		: params
			,success	: function(opt,result){
				if(result.data){
					$("#payResultFrm").submit();
				}else{
					alert("결제 실패?");
					return;
				}
			}
		}
		AjaxUtil.post(p);
	}
}
</script>

<!-- 결제 테스트 -->
<form name="payTestFrm" id="payTestFrm" method="post" action="/front/pay/payTest">
	<input type="hidden" name="orderNo" value="ORDNO"/>
	<input type="hidden" name="masterSeq" value="${searchUserInfo.masterSeq }"/>
	<input type="hidden" name="payType" value="CARD"/>
	<input type="hidden" name="seqNo" value="SEQNO"/>
	<input type="hidden" name="approvalNo" value="APPROVALNO"/>
	<input type="hidden" name="id" value="HYND"/>
	<input type="hidden" name="name" value="현대카드"/>
	<input type="hidden" name="sellMm" value="0"/>
	<input type="hidden" name="amt" id="testAmt"/>
</form>

<form name="payResultFrm" id="payResultFrm" method="post" action="/front/pay/payResult">
	<input type="hidden" name="masterSeq" value="${searchUserInfo.masterSeq }"/>
</form>

<div class="inquiry_wrap type2">
	<div class="title">모집인 결제</div>
	<div class="payment_wrap">
		<div class="inner">
			<table class="member_info">
				<colgroup>
					<col width="210">
					<col width="*">
				</colgroup>
				<tbody>
					<tr>
						<th>계약금융회사</th>
						<td>${searchUserInfo.comCodeNm }</td>
					</tr>
<%-- 					<tr>
						<th>취급상품</th>
						<td>${searchUserInfo.plProductNm }</td>
					</tr> --%>
					<c:choose>
						<c:when test="${searchUserInfo.plClass eq '1' }">
							<tr>
								<th>성명</th>
								<td>${searchUserInfo.plMName }</td>
							</tr>
							<tr>
								<th>휴대폰번호</th>
								<td>${searchUserInfo.plCellphone }</td>
							</tr>
							<tr>
								<th>생년월일</th>
								<td>${searchUserInfo.plMZIdFront }</td>
							</tr>
							<tr>
								<th>성별</th>
								<td>
									<c:choose>
										<c:when test="${searchUserInfo.gender eq 'M' }">남성</c:when>
										<c:otherwise>여성</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<th>결제금액</th>
								<td><span class="bold red">20,000원</span></td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>법인번호</th>
								<td>${searchUserInfo.plMerchantNo }</td>
							</tr>
							<tr>
								<th>대표자 성명</th>
								<td>${searchUserInfo.plCeoName }</td>
							</tr>
							<tr>
								<th>결제금액</th>
								<td><span class="bold red">200,000원</span></td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<div class="bottom_box mgt30">
			<ul>
				<li>
					<span class="dot"></span>
					<p>조회 결과입니다. 아래 내용을 최종 확인하시고 결제하기 버튼을 눌러서 결제 진행하시면 됩니다.</p>
				</li>
				<li>
					<span class="dot"></span>
					<p>결제완료 후 모집인 모집인 등록증을 확인 및 다운로드 받으 실 수 있습니다.</p>
				</li>
				<li>
					<span class="red_dot"></span>
					<p class="red">결제 후 환불은 불가능 합니다.</p>
				</li>
			</ul>
		</div>
	</div>
	<div class="btn_wrap">
		<!-- <a href="javascript:void(0);" class="btn_black_long" onclick="goPayTest();">결제하기(테스트)</a> -->
		<a href="javascript:void(0);" class="btn_black_long" onclick="ftn_approval(document.fm);">결제하기</a>
	</div>
</div>

<jsp:include page="allat_include.jsp"></jsp:include>

