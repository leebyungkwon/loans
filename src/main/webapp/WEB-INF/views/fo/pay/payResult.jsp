<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
	<div class="title">결제결과</div>
	<div class="payment_wrap">
		<div class="inner">
			<table class="member_info">
				<colgroup>
					<col width="210">
					<col width="*">
				</colgroup>
				<tbody>
					<c:choose>
						<c:when test="${resultCode ne '0000' }">
							<tr>
								<th>결과코드</th>
								<td>${resultCode}</td>
							</tr>
							<tr>
								<th>결과메세지</th>
								<td>${resultMessage}</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>등록번호</th>
								<td>${payResultInfo.plRegistNo }</td>
							</tr>
							<tr>
								<th>계약금융회사</th>
								<td>${payResultInfo.comCodeNm }</td>
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
									<tr>
										<th>결제</th>
										<td><span class="bold red">${payResultInfo.amt }원</span></td>
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
									<tr>
										<th>결제</th>
										<td><span class="bold red">${payResultInfo.amt }원</span></td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</div>
<!-- 	<div class="btn_wrap">
		<a href="javascript:void(0);" class="btn_black_long" onclick="goCertiCardDownload();">등록증 다운로드</a>
	</div> -->
</div>
      
      