<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script type="text/javascript">

	function pageLoad(){
		
		// 글 목록 버튼
		$("#companyCodeBtn").on("click", function(){
			location.href = "/admin/company/companyCodePage";
		});

		// 글 등록 버튼
		$("#companyCodeSaveBtn").on("click", function(){
			if(confirm("등록 하시겠습니까?")){
				$("#companyCodeRegFrm").attr("action","/admin/company/saveCompanyCodeDetail");
				$("#comCode").val("0"); // 기존 시퀀스 초기화
				var saveCompanyCodeDetailParam = {
					name : 'companyCodeRegFrm'
					,success: function(opt, result) {
						if(result.data == 0) {
							$("#plMerchantNo").val("");
						} else {
							location.href = "/admin/company/companyCodePage";
						}
					}
				}      
				AjaxUtil.files(saveCompanyCodeDetailParam);
			}
		});

		// 글 수정 버튼
		$("#companyCodeUpdBtn").on("click", function(){
			if(confirm("수정 하시겠습니까?")){
				$("#companyCodeRegFrm").attr("action","/admin/company/updCompanyCodeDetail");
				var updCompanyCodeDetailParam = {
					name : 'companyCodeRegFrm'
					,success: function(opt, result) {
						if(result.data == 0) {
							$("#plMerchantNo").val("");
						} else {
							location.href = "/admin/company/companyCodePage";
						}
					}
				}      
				AjaxUtil.files(updCompanyCodeDetailParam);
			}
		});

		// 글 삭제 버튼
		$("#companyCodeDelBtn").on("click", function(){
			if(confirm("삭제 하시겠습니까?")){
				var param = {
					'comCode' : $("#comCode").val()
				}
				var p = {
					param: param
					,url: "/admin/company/delCompanyCodeDetail"
					,success: function(opt, result) {
						if(result > 0) {
							alert("삭제를 실패하셨습니다.");
						} else {
							alert("삭제를 성공적으로 완료하였습니다.");
							location.href = "/admin/company/companyCodePage";
						}  
					}
				}      
				AjaxUtil.post(p);
			}
		});

	}
	
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title type2">
			<h2>회원사 관리</h2>
		</div>
	</div>
	<form name="companyCodeRegFrm" id="companyCodeRegFrm" method="POST" >
		<input type="hidden" name="comCode" value="${companyCodeInfo.comCode}"/>
		<div class="contents">
			<div id="table">
				<table class="view_table">
					<tr>
					<c:choose>
						<c:when test="${!empty companyCodeInfo}">
							<th>회원사(번호)</th>
							<td colspan="3">
								<input type="text" id="comCode" name="comCode" value="${companyCodeInfo.comCode}" readonly="readonly" class="w60" />
							</td>
						</c:when>
					</c:choose>
					</tr>
					<tr>
						<th>회원사(상호명)</th>
						<td colspan="3">
							<input type="text" id="comName" name="comName" placeholder="회원사(상호명)을 입력해 주세요!"  value="${companyCodeInfo.comName}" class="w60" data-vd='{"type":"text","len":"1,20","req":true,"msg":"회원사(상호명)을 다시 입력해 주세요"}'/>
						</td>
					</tr>
					<tr>
						<th>법인등록번호</th>
						<td colspan="3">
							<input type="text" id="plMerchantNo" name="plMerchantNo" placeholder="법인등록번호 14자리를 입력해 주세요. ( -포함)"   value="${companyCodeInfo.plMerchantNo}" class="w60" data-vd='{"type":"plMerchant","len":"14,14","req":true,"msg":"법인번호( -포함) 14자리를 입력해 주세요."}' />
						</td>
					</tr>
					<tr>
						<th>사업자등록번호</th>
						<td colspan="3">
							<input type="text" id="plBusinessNo" name="plBusinessNo"  value="${companyCodeInfo.plBusinessNo}" class="w60" placeholder="사업자등록번호 12자리를 입력해 주세요. ( -포함)"  data-vd='{"type":"plBusiness","len":"12,12","req":true,"msg":"사업자등록번호( -포함) 12자리를 입력해 주세요."}' />
						</td>
					</tr>
					<tr>
						<th>회사대표번호</th>
						<td colspan="3">
							<input type="text" id="compPhoneNo" name="compPhoneNo" placeholder="회사대표번호를 입력해 주세요. ( -포함)"   value="${companyCodeInfo.compPhoneNo}" class="w60"  data-vd='{"type":"extensionNo","len":"1,20","req":true,"msg":"회사대표번호를 입력해 주세요!"}'  />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
	
	<sec:authorize access="hasAnyRole('SYSTEM', 'ADMIN')" >
		<div class="btn_wrap">
			<a href="javascript:void(0);" id="companyCodeBtn"  class="btn_gray">목록</a>								
			<c:choose>
				<c:when test="${!empty companyCodeInfo}">
					<a href="javascript:void(0);" id="companyCodeUpdBtn"  class="btn_blue btn_right02">수정</a>
					<a href="javascript:void(0);" id="companyCodeDelBtn"  class="btn_Lgray btn_right">삭제</a>		
				</c:when>
				<c:otherwise>
					<a href="javascript:void(0);" id="companyCodeSaveBtn"  class="btn_blue btn_right">등록</a>
				</c:otherwise>
			</c:choose>
		</div>
	</sec:authorize>
</div>