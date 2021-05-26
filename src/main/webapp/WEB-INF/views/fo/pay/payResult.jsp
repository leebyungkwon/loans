<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad() {
	alert("masterSeq :: "+"${masterSeq}");
}
</script>

<div class="inquiry_wrap type2">
	<div class="title">결제내역 및 등록증 다운로드</div>
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
						<td>우리금융캐피탈</td>
					</tr>
					<tr>
						<th>성명</th>
						<td>홍길동</td>
					</tr>
					<tr>
						<th>휴대폰번호</th>
						<td>01067384411</td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td>790625</td>
					</tr>
					<tr>
						<th>성별</th>
						<td>남성</td>
					</tr>
					<tr>
						<th>결제금액</th>
						<td><span class="bold">20,000원</span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="btn_wrap">
		<a href="javascript:void(0);" class="btn_black_long">등록증 다운로드</a>
	</div>
</div>
      
      