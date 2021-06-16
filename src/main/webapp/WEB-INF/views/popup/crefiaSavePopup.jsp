<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
				<select id="popCreGrp" name="popCreGrp"></select>
			</td>
		</tr>
		<tr>
			<th>이름</th>
			<td>
				<input type="text" class="w50 file_input" id="popMemberName" name="memberName" placeholder="담당자명을 입력해 주세요." value="${result.memberName}" data-vd='{"type":"text","len":"1,20","req":true,"msg":"담당자명을 입력해 주세요"}'>
			</td>
		</tr>
		<c:choose>
			<c:when test="${!empty result}">
				<tr>
					<th>아이디</th>
					<td>
						<input type="text" class="w50 file_input" id="popMemberId" name="memberId" value="${result.memberId}" readonly="readonly" maxlength="11" data-vd='{"type":"id","len":"5,11","req":true,"msg":"아이디를 입력해 주세요"}'/>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<th>아이디</th>
					<td>
						<input type="text" class="w50 file_input" id="popMemberId" name="memberId" placeholder="아이디를 입력해 주세요." maxlength="11" data-vd='{"type":"id","len":"5,11","req":true,"msg":"아이디를 입력해 주세요"}'/>
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<tr>
			<th>비밀번호</th>
			<td>
				<input type="password" class="w50 file_input" id="popPassword" name="password" placeholder="8자리~20자리 (2종류 이상의 문자구성)" maxlength="20" data-vd='{"type":"text","len":"8,20","req":true,"msg":"비밀번호를 다시 입력해 주세요"}'/>
				<p class="noti" style="margin-top: 5px;">
					※ 알파벳 대문자, 알파벳 소문자, 특수문자, 숫자 중 2종류 이상을 선택하여 문자를 구성해야 합니다.<br>
					※ 아이디, 동일한 문자의 반복 및 연속된 3개의 숫자/문자는 사용이 불가능 합니다.
				</p>
			</td>
		</tr>
		<tr>
			<th>비밀번호확인</th>
			<td>
				<input type="password" class="w50 file_input" id="popPasswordChk" placeholder="동일한 비밀번호를 입력" maxlength="20" data-vd='{"type":"text","len":"8,20","req":true,"msg":"동일한 비밀번호를 입력해 주세요"}'/>
			</td>
		</tr>
	</tbody>
</table>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="saveCrefia();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopup();">취소</a>
</div>

