<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
function pageLoad() {
	//alert("masterSeq >> "+"${masterSeq}");
}

//등록증 다운로드 팝업
function goCertiCardDownload(){
	let p = {
	  	  id 		: "certiCardDownloadPop"
		, url 		: "/front/pay/certiCardDownloadPopup"
	}
	PopUtil.openPopup(p);
}
</script>

<div class="inquiry_wrap type2">
	<div class="title">결제내역</div>
	<div class="payment_wrap">
		<div class="inner">
			<table class="member_info">
				<colgroup>
					<col width="210">
					<col width="*">
				</colgroup>
				<tbody>
					<tr>
						<th>등록번호</th>
						<td>${payResultInfo.plRegistNo }</td>
					</tr>
					<c:choose>
						<c:when test="${payResultInfo.plClass eq '1' }">
							<tr>
								<th>성명</th>
								<td>${payResultInfo.plMName }</td>
							</tr>
							<tr>
								<th>휴대폰번호</th>
								<td>${payResultInfo.plCellphone }</td>
							</tr>
							<tr>
								<th>생년월일</th>
								<td>${payResultInfo.plMZIdFront }</td>
							</tr>
							<tr>
								<th>성별</th>
								<td>
									<c:choose>
										<c:when test="${payResultInfo.gender eq 'M' }">남성</c:when>
										<c:otherwise>여성</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>법인번호</th>
								<td>${payResultInfo.plMerchantNo }</td>
							</tr>
							<tr>
								<th>대표자 성명</th>
								<td>${payResultInfo.plCeoName }</td>
							</tr>
						</c:otherwise>
					</c:choose>
					<tr>
						<th>결제금액</th>
						<td>
							<span class="bold red">
								<fmt:formatNumber value="${payResultInfo.amt }" pattern="#,###"/> 원
							</span>
						</td>
					</tr>
					<tr>
						<th>결제일시</th>
						<td>${payResultInfo.payDate }</td>
					</tr>
					<tr>
						<th>은행</th>
						<td>${payResultInfo.payName }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
<!-- 	<div class="btn_wrap">
		<a href="javascript:void(0);" class="btn_black_long" onclick="goCertiCardDownload();">등록증 다운로드</a>
	</div> -->
</div>
      
      