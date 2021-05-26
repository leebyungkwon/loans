<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
function pageLoad() {
	
}
</script>

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
						<td><span class="bold red">20,000원</span></td>
					</tr>
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
		<a href="javascript:void(0);" class="btn_black_long" onclick="ftn_approval(document.fm);">결제하기</a>
	</div>
</div>

<jsp:include page="allat_include.jsp"></jsp:include>

