<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script>

var companyDetailGrid = Object.create(GRID);

function pageLoad(){
	companyDetailGrid.set({
		  id		: "companyDetailGrid"
		, url		: "/admin/company/companyDetail"
	    , width		: "100%"
		, headCol	: ["","회원사", "아이디", "부서명", "담당자명", "직위", "이메일","전화번호","회원가입일", "승인상태","첨부파일"]
		, bodyCol	: 
			[
				 {type:"string"	, name:'memberSeq'		, index:'memberSeq'		, hidden:true  	, id:true		}
				,{type:"string"	, name:'comCode'		, index:'comCode'		, width:"15%"	}
				,{type:"string"	, name:'memberId'		, index:'memberId'		, width:"15%"	, align:"center"}
				,{type:"string"	, name:'deptNm'			, index:'deptNm'		, width:"15%"	, align:"center"}		
				,{type:"string"	, name:'memberName'		, index:'memberName'	, width:"10%"	, align:"center"}		
				,{type:"string"	, name:'positionNm'		, index:'positionNm'	, width:"10%"	, align:"center"}
				,{type:"string"	, name:'email'			, index:'email'			, width:"10%"	, align:"center"}
				,{type:"string"	, name:'mobileNo'		, index:'mobileNo'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'joinDt'			, index:'joinDt'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'apprYn'			, index:'apprYn'		, width:"10%"	, align:"center"}
				,{type:"string"	, name:'fileSeq'		, index:'fileSeq'		, width:"10%"	, align:"center"}
			]
		, sortNm : "com_code"
		, sort : "DESC"
		, size : 10
		, check : true
		, rowClick	: {color:"#ccc", retFunc : detailPop}
		, gridSearch : "search,searctBtn"
		, isPaging : true
	});
}

function detailPop(idx, data){
/* 	let p = {
		id : "dtl_save"
		, data : data
		, url : "/member/company/p/companyView"
	}
	LibUtil.openPopup(p); */
	
	/* submit으로 변경되었습니다. */
}

</script>


<div id="container">
			<div class="gnb">
				<ul>
					<li class="on">
						<a href="#">모집인 조회 및 변경</a>
					</li>
					<li>
						<a href="#">모집인 등록</a>
					</li>
					<li>
						<a href="#">관리자 조회 및 변경</a>
					</li>
					<li>
						<a href="#">공지사항</a>
					</li>
				</ul>
			</div>

			<div class="cont_area">
				<div class="top_box">
					<div class="title type2">
						<h2>회원사 담당자 관리</h2>
					</div>

				</div>

				<div class="contents">
					<h3>등록정보</h3>
						<div id="table">
							<table class="view_table">
								<tr>
									<th>회원사</th>
									<td>볼보파이낸셜</td>
									<th>담당자</th>
									<td>담당자1 (<a href="abc@gmail.com">abc@gmail.com</a>, 010-12345678)</td>
								</tr>
								<tr>
									<th>모집인 상태</th>
									<td>자격취득(결제완료) <a href="#" class="btn_Lgray btn_small mgl5">이력보기</a></td>
									<th>결제여부</th>
									<td>결제완료 (국민카드 / 2021.10.20)</td>
								</tr>
								<tr>
									<th>처리상태</th>
									<td colspan="3">완료</td>
								</tr>
								<tr>
									<th>모집인 분류</th>
									<td colspan="3">개인</td>
								</tr>
								<tr>
									<th>신규경력구분</th>
									<td colspan="3">신규</td>
								</tr>
								<tr>
									<th>금융상품유형</th>
									<td colspan="3">대출</td>
								</tr>
								<tr>
									<th>이름</th>
									<td><input type="text" class="w100" value="홍길동"></td>
									<th>주민번호</th>
									<td><input type="text" class="w100" value="801234-1234567"></td>
								</tr>
								<tr>
									<th>휴대폰번호</th>
									<td colspan="3"><input type="text" class="w100" value="010-1234-5678"></td>
								</tr>
								<tr>
									<th>주소</th>
									<td colspan="3"><input type="text" class="w100" value="서울시 서초구 강남대로 55길 9-15 삼성아파트 101동 1402호"></td>
								</tr>
								<tr>
									<th>교육이수번호</th>
									<td colspan="3"><input type="text" class="w100" value="20210221315"></td>
								</tr>
								<tr>
									<th>경력시작일</th>
									<td><input type="text" class="w100" value="2021-01-01"></td>
									<th>경력종료일</th>
									<td>2021-05-22</td>
								</tr>
								<tr>
									<th>계약일자</th>
									<td colspan="3"><input type="text" class="w50" value="2021-01-01"></td>
								</tr>
								<tr>
									<th>위탁예정기간</th>
									<td colspan="3"><input type="text" class="w50" value="2021-01-01"></td>
								</tr>
								<tr>
									<th>변경요청사항</th>
									<td colspan="3"><input type="text"class="w100"  value="변경된 사항을 적어주세요."></td>
								</tr>
							</table>
						</div>

						<h3>첨부서류</h3>
							<div id="table02">
								<table class="view_table">
									<colgroup>
										<col width="50%"/>
										<col width="50%"/>
									</colgroup>
									<tr>
										<th class="acenter">주민등록증사본, 여권사본 및 여권정보증명서, 운전면허증 사본 중 택1일*</th>
										<td>
											<input type="text"class="w50 file_input"  value="주민등록증사본.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">교육과정 이수확인서 (경력) *</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지1.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">경력증명서*</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지2.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">교육과정 인증서 (신규)*</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지3.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">금융상품 유형, 내용에 대한 설명자료*</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지4.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">결격사유 확인서 (파산, 피한정후견인등)*</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지5.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
									<tr>
										<th class="acenter">대리인 신청 위임장(위임인 인간날인) 및 위임인 인감증명서</th>
										<td>
											<input type="text"class="w50 file_input"  value="이미지6.jpg" readonly disabled>
											<a href="#" class="btn_gray btn_del mgl10">삭제</a>
											<a href="#" class="btn_black btn_small mgl5">파일찾기</a>
										</td>
									</tr>
								</table>
							</div>
						<div class="btn_wrap">
							<a href="#" class="btn_gray">이전</a>
							<a href="#" class="btn_black btn_right">변경요청하기</a>
						</div>
					</div>
				</div>

		<div id="companyDetailGrid">

		</div>
	</div>