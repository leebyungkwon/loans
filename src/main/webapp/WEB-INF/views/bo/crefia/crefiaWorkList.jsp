<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
 function pageLoad(){
	/*
	//중복 클릭 방지
	$(".crefia").click(function(){
		if($('input[type="checkbox"][data-comCode="'+$(this).attr("data-comCode")+'"]:checked').length > 1){
			$('input[type="checkbox"][data-comCode="'+$(this).attr("data-comCode")+'"]').prop("checked",false);
			$(this).prop("checked",true);
		}
	}); 
	*/
	
	//저장 버튼 클릭 이벤트
	$("#saveCrefiaWorkBtn").click(function(){
		var comCodeArr 		= [];
		var memberSeqArr 	= [];
		var noArr 			= [];
		var uniqueArr 		= [];
		
		$(".crefia").each(function(index){
			if($(this).is(":checked")){
				comCodeArr.push($(this).attr("data-comCode"));
				memberSeqArr.push($(this).val());
			}else if($('input[type="checkbox"][data-comCode="'+$(this).attr("data-comCode")+'"]:checked').length == 0){ 
				noArr.push($(this).attr("data-comName"));
			}
		});
		//선택안한 회원사 중복제거
		uniqueArr = noArr.filter(function(item, pos, self) {
			return self.indexOf(item) == pos;
		});
		if(uniqueArr.length > 0){
			var msg = '';
			for(var i = 0;i < uniqueArr.length;i++){
				msg += uniqueArr[i]+'의 업무 담당자를 선택해 주세요.\n';
			}
			alert(msg);
			return;
		}
		//저장
		if(confirm("저장하시겠습니까?")){
			var param = {
				 'memberSeqArr' : memberSeqArr
				,'comCodeArr'  	: comCodeArr
			}
			var p = {
				 param 		: param 
				,url 		: "/admin/crefiaWork/insertCrefiaWork"
				,success	: function(opt,result){
					location.reload();
				}	
			}
		}
		AjaxUtil.post(p);
	});
}  
</script>

<div class="cont_area">
	<div class="top_box">
		<div class="title">
			<h2>협회 관리자 업무분장</h2>
		</div>
	</div>
	<div class="contents">
		<div id="table" style="height: 670px; overflow: scroll;">
			<table>
				<thead>
					<tr>
						<th></th>
 						<c:forEach items="${memberInfo}" var="memberInfo" varStatus="status"> 
							<th><c:out value="${memberInfo.memberName}"/></th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${companyInfo}" var="companyInfo" varStatus="status">
						<tr>
							<td align="center"><c:out value="${companyInfo.comName}"/></td>
							<c:forEach items="${memberInfo}" var="memberInfo" varStatus="status">
								<td>
									<c:set var="memberSeq" value="|${memberInfo.memberSeq }|"></c:set>
									<input type="checkbox" name="check" id="comCode" class="crefia" value="${memberInfo.memberSeq}" data-comCode="${companyInfo.comCode}" data-comName="${companyInfo.comName}" <c:if test="${fn:contains(companyInfo.chkedMemberSeq, memberSeq) }">checked="checked"</c:if> >
								</td>
							</c:forEach>
						</tr>   
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="btn_wrap">
			<a href="javascript:void(0);" class="btn_black btn_right" id="saveCrefiaWorkBtn">저장</a>
		</div>
	</div>
</div>