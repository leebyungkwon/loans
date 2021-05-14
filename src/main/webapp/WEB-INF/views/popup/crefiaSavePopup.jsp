<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<script type="text/javascript">

	// 협회 관리자 저장
	function crefiaSave(){
		
	}

</script>

<div class="title_wrap">
	<h5>협회 관리자 상세1</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>

<form name="crefiaSaveFrm" id="crefiaSaveFrm" method="post">
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>그룹선택</th>
				<td>
					<input type="hidden" id="hiddenCreGrp" value="${result.creGrp}"/>
					<select id="popCreGrp" name="popCreGrp" style="width:50%;">
						<option value="">전체</option>
						<option value="2">관리자</option>
						<option value="1">실무자</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>이름</th>
				<td class="file">
					<input type="text" class="w50 file_input" id="popMemberName" name="popMemberName" value="${result.memberName }">
				</td>
			</tr>
			<tr>
				<th>아이디</th>
				<td class="file">
					<input type="text" class="w50 file_input" id="popMemberId" name="popMemberId" value="${result.memberId }">
				</td>
			</tr>
			<tr>
				<th>패스워드</th>
				<td class="file">
					<input type="password" class="w50 file_input" id="popPassword" name="popPassword" data-vd='{"type":"pw","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}'>
				</td>
			</tr>
			<tr>
				<th>패스워드확인</th>
				<td class="file">
					<input type="password" class="w50 file_input">
				</td>
			</tr>
		</tbody>
	</table>
</form>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="crefiaSave();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

