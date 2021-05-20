<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<script type="text/javascript">

</script>

<div class="title_wrap">
	<h5>협회 관리자 상세</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopup();"></a>
</div>
<input type="hidden" id="hiddenMemberSeq" value="${result.memberSeq}" />
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
					<option value="1">실무자</option>
					<option value="2">관리자</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>이름</th>
			<td class="file">
				<input type="text" class="w50 file_input" id="popMemberName" name="memberName" value="${result.memberName }" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'>
			</td>
		</tr>
		<tr>
			<th>아이디</th>
			<td class="file">
				<input type="text" class="w50 file_input" id="popMemberId" name="memberId" value="${result.memberId }" maxlength="11" data-vd='{"type":"id","len":"5,11","req":true,"msg":"아이디를 입력해 주세요"}'/>
			</td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td class="file">
				<input type="password" class="w50 file_input" id="popPassword" name="password" placeholder="최소8자, 문자/숫자/특수문자를 입력하세요." maxlength="20" data-vd='{"type":"pw","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}'/>
			</td>
		</tr>
		<tr>
			<th>비밀번호확인</th>
			<td class="file">
				<input type="password" class="w50 file_input" id="popPasswordChk" placeholder="동일한 비밀번호를 입력" maxlength="20" data-vd='{"type":"pw","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}'/>
			</td>
		</tr>
	</tbody>
</table>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="saveCrefia();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

