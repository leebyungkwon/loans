<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">

 function pageLoad(){
		
	$("#saveCrefiaWorkBtn").click(function(){
		var comCodeArr = [];
		var memberSeqArr = [];
		$(".crefia").each(function(index){
			if($(this).is(":checked")){
				comCodeArr.push($(this).attr("data-comCode"));
				memberSeqArr.push($(this).val());
			}
		});
		
		var comCodeSet = new Set(comCodeArr);
		
		if(comCodeArr.length != comCodeSet.size){
			alert("중복 확인 바랍니다.");
			return false;
		}
		
		if(comCodeArr.length != "${fn:length(companyInfo)}"){
			alert("클릭 안된곳 있어요");
			return false;
		}
		if(confirm("저장하시겠습니까?")){
			
			var param = {
					 'memberSeqArr' : memberSeqArr
					,'comCodeArr'  : comCodeArr
			}
			var p = {
					param : param 
				   ,url : "/admin/crefiaWork/insertCrefiaWork"
				   ,success:function(opt,result){
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
		<div id="table">
			<table>
				<thead>
					<tr>
					
						<th>회원사명</th>
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
								<td><input class="crefia" name="check" type="checkbox" id="comCode"  value="${memberInfo.memberSeq}" data-comCode="${companyInfo.comCode}" >${memberInfo.comCode}</td>
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