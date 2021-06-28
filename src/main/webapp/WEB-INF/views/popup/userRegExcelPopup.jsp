<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="title_wrap">
	<h5>모집인 등록</h5>
	<a href="javascript:void(0);" class="pop_close" onclick="PopUtil.closePopupByReload();"></a>
</div>
<p class="popup_desc">
	모집인 유형에 따른 샘플파일을 다운로드 하시고 해당 양식에 따라 등록되어야 합니다.<br />
	엑셀 업로드 이후에는 각 모집인별로 첨부파일을 등록완료 후에 승인신청을 하셔야 합니다.<br />
	엑셀파일과 첨부파일 업로드 이후 등록신청을 하지 않고 1개월이 경과되는 경우에는 자동삭제 처리됩니다.<br />
	신규 모집인은 평가일로부터 1주일 후에 업로드 가능합니다.
</p>
<form name="userRegInfoInsertFrm" id="userRegInfoInsertFrm" method="post" enctype="multipart/form-data">
	<table class="popup_table">
		<colgroup>
			<col width="170">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>모집인유형 *</th>
				<td>
					<div class="input_radio_wrap">
						<input type="radio" name="plClass" id="radio01" value="1" >
						<label for="radio01">개인</label>
					</div>
					<div class="input_radio_wrap mgl20">
						<input type="radio" name="plClass" id="radio02" value="2" >
						<label for="radio02">법인</label>
					</div>
					<div class="f-right fsize11">※ 모집인 유형 클릭 시 샘플 다운로드 버튼이 생성됩니다.</div>
				</td>
			</tr>
			<tr>
				<th>엑셀업로드 *</th>
				<td class="file" id="sampleCheck">
					<input type="text" class="w50 file_input" readonly disabled>
					<input type="file" name="files" id="userRegFile" onchange="goFileNmShow();" style="display: none;"/>
					<a href="javascript:void(0);" class="btn_black btn_small mgl5" onclick="$('#userRegFile').click();">파일찾기</a>
				</td>
			</tr>
		</tbody>
	</table>
</form>
<p class="popup_desc">
	※ 모집인 등록 엑셀 서식 작성요령<br /><br />
	[공통]<br />
	- 구분 : 신규 = 1 / 경력 = 2<br />
	- 금융상품유형 : 등록하고자 하는 금융상품유형이 복수인 경우 개별 건별로 신청서 작성<br />
	- 금융상품유형별 코드 : <span class="red">대출 = 1 / 리스 = 2 / TM대출 = 3 / TM리스 = 4 (등록시스템 전체 적용)</span><br />
	- 주민등록번호, 법인등록번호, 휴대폰번호, 일자 입력 시 중간에 “-” 입력<br />
	- 주소,본점소재지 코드 : 서울 = 1 / 경기 = 2 / 충청북도 = 3 / 충청남도 = 4 / 강원도 = 5<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;경상북도 = 6 / 경상남도 = 7 / 전라북도 = 8 / 전라남도 = 9 / 인천 = 10<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;세종 = 11 / 대전 = 12 / 대구 = 13 / 울산 = 14 / 광주 = 15<br />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;부산 = 16 / 제주 = 17<br />
	- 위탁예정기간 : 현재시점의 계약 종료일 기재<br /><br />
	[개인]<br />
	- 법인등록번호 : 법인 소속 모집인의 경우에만 소속 모집법인 정보 기재<br />
	- 경력시작일,경력종료일 : 경력증명서 내용 중 금소법상 인정되는 경력의 시작일과 종료일을 작성(대출성상품을 취급하는 금융상품직접판매업에서 3년이상 경력이 있는 경우에만 작성)<br /><br />
	[법인]<br />
	- 자본금 : 백만원 단위로 기재<br />
	- 본점상세주소 : 법인등기부등본상 주소와 일치하도록 작성
</p>
<div class="popup_btn_wrap">
	<a href="javascript:void(0);" class="pop_btn_black" onclick="goUserRegInfoExcelUpload();">저장</a>
	<a href="javascript:void(0);" class="pop_btn_white" onclick="PopUtil.closePopupByReload();">취소</a>
</div>

