<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
var Grid = Object.create(GRID); 

function pageLoad(){
	console.log(Grid);
	Grid.set({
		  id		: "grid1"
  		, url		: "/team/teamList"
	    , width		: "600px"
  		, headCol	: ["ID", "팀명", "국가명", "도시", "메모"]
  		, bodyCol	: 
  			[
				{type:"string"	, name:'team_id'			, index:'team_id'			, width:"0px"	, hidden:true	}
			,	{type:"string"	, name:'team_kor_nm'		, index:'team_kor_nm'		, width:"200px"	, hidden:false	, align:"right"}
			,	{type:"string"	, name:'nation_kor_nm'		, index:'nation_kor_nm'		, width:"200px"	, hidden:false	, align:"right"}		
			,	{type:"string"	, name:'team_hometown'		, index:'team_hometown'		, width:"100px"	, hidden:false	, sortable:false}		
			,	{type:"text"	, name:'team_memo'			, index:'team_memo'			, width:"100px"	, hidden:false	, sortable:false}		
			],
	});
}
</script>

<div class="article_right">
	<h3  class="article_tit ">공지사항</h3>
	<%-- 
	<div class="tab" id="grid1></div>
	--%>
	
	<div class="faq_search pc_view">
		<div class="btnbx">
			<a class="btn btn_home" href="/bo/board/reg">등록</a>
		</div>
	</div>
	
	<div class="tab">
		<ul>
			<li class="selected">
				<a href="javascript:">공지사항</a>
			</li> 
			<li class="">
				<a href="javascript:">이벤트 당첨안내</a>
			</li>
		</ul>
	</div>
	
	<div class="my_tbl_lst my_tbl_notice">
	
		<div class="my_tbl_tit">
			<div class="my_tbl_info">
				<p class="type">번호</p>
				<p class="tit">제목</p>
				<p class="date">등록일</p>
			</div>
		</div>
		<div class="">
			<div class="my_tbl">
				
				<div class="my_tbl_info">
					<div class="infoinner">
						<p class="type"><span class="blind">번호</span>3</p>
						<p class="tit"><span class="blind">제목</span>[공지] 29CM 강남 스토어 영업 종료</p>
						<p class="date"><span class="blind">등록일</span>2021-02-24</p>
					</div>
				</div>

				<div class="reply_row">
					<div class="notice_wrap">
						<div>
							<p><p>29CM 강남 스토어가 오는 2월 27일 부로 866일 간의 영업을 마칩니다.&nbsp;</p><p><br></p><p>'더 나은 선택을 더 가까이에서' 제안드리기 위해</p><p>29CM는 2018년 10월 16일, 첫 오프라인 프로젝트인 '29CM STORE'를 열었습니다.</p><p><br></p><p>지난 4년 간 강남 스토어는 총 292개 브랜드와 함께하며,</p><p>30개의 팝업스토어와 40번의 이벤트를 열었습니다.</p><p><br></p><p>때로는 좋아하는 브랜드를 만나러, 때로는 약속 시간 사이 여유를 즐기러</p><p>스토어를 찾아주신 18만 명의 고객 여러분, 함께해주셔서 고마웠습니다.</p><p><br></p><p>헤어짐은 아쉽지만 더 가까운 곳에서 흥미로운 경험과 브랜드를</p><p>소개해드리기 위한 29CM의 노력은 계속됩니다.</p><p><br></p><p>멋지고, 착하고, 엉뚱한 29CM의 다음 오프라인 프로젝트를 기대해주세요.&nbsp;</p></p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="">
			<div class="my_tbl">
				<div class="my_tbl_info">
					<div class="infoinner">
						<p class="type"><span class="blind">번호</span>2</p>
						<p class="tit"><span class="blind">제목</span>[공지] 한진택배 일부 지역 배송 불가 및 반품 수거 중단 안내</p>
						<p class="date"><span class="blind">등록일</span>2021-02-23</p>
					</div>
				</div>

				<div class="reply_row">
					<div class="notice_wrap">
						<div>
							<p><p>조금 더 다르게 조금 더 가깝게 29CM 입니다.&nbsp;</p><p><br></p><p>한진택배 일부 지역의 택배 노조 파업으로</p><p>택배 서비스가 원활하지 않으며</p><p>파업이 확정된 지역은 배송이 불가하여</p><p>별도로 주문 취소 안내를  드릴 수 있는 점 안내드립니다.</p><p><br></p><p>[ 택배노조 파업 예상 지역 ]</p><p><br></p><p>1) 수도권 지역</p><p>- 경기도 성남시 중원구/ 수정구 전체</p><p>- 경기도 광주시 송정동, 쌍령동, 초월읍, 오포읍, 곤지암읍, 중부면, 남종면, 퇴촌면, 도척면, 회덕리</p><p>- 이천시 전체</p><p>- 고양시 덕양구 주교동, 성사동, 화정동, 행신동, 도내동, 동산동, 지축동, 고양동, 벽제동</p><p>&nbsp;</p><p>2) 지방권역&nbsp;</p><p>- 울산광역시 남구/중구/동구</p><p>- 거제시 전체</p><p>- 김천시 신음동, 부곡동, 삼락동, 교동, 다수동, 백옥동, 양천동, 문당동, 율곡동, 감문면,&nbsp;</p><p>&nbsp;&nbsp;개령면, 어모면, 봉산면, 대항면, 감천면, 조마면, 대덕면, 구성면, 지례면, 부항면, 증산면</p><p><br></p><p>이번 노조 파업으로 한진택배를 이용하는</p><p>브랜드사 상품의 배송 불가 및 반품 상품의 수거 접수가 늦어지고 있으며</p><p>한진택배를 통한 교환/반품 상품을 수거 예정이었던 고객님들께서는</p><p>다른 택배사를 이용하여 반품을 부탁드립니다.</p><p>&nbsp;</p><p>추가로 궁금하신 내용은1:1 문의하기를 이용해 주세요.</p><p>1:1문의▶https://goo.gl/qMBHUc</p><p><br></p><p>Guide to better choice 우리는 더 나은 선택을 돕습니다.</p><p>멋지고 착하고 엉뚱한</p><p>another select - shop&nbsp;29CM</p></p>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div  class="custom-pagination">
		    <span  class="pagination-previous">
		        <a  class="">
		        	<
		        </a>
		    </span>
		    <span class="num">
		        <a  class="">
		            1
		        </a>
		    </span><span  class="num current">
		        <span  class="">
		            2
		        </span>
		    </span>
		    <span class="pagination-next">
		        <a  class="">
		        	>
		        </a>
		    </span>
		</div>
	</div>
</div>