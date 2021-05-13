package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("user")
public class UserDomain extends BaseDomain {

	//모집인정보(tb_lc_mas01)
	private int masterSeq;			//시퀀스
	private int comCode;			//담당회원사코드
	private int memberSeq;			//담당자시퀀스
	private String plClass;			//분류							-> [CLS001]개인,법인
	private String careerTyp;		//구분							-> [CAR001]신규,경력
	private String plProduct;		//금융상품유형(취급상품)				-> [PRD001]대출,시설대여 및 연불판매,할부,어음할인,매출채권매입,지급보증,기타 대출성 상품
	private String plRegStat;		//모집인상태						-> [REG001]승인전,승인완료,자격취득,해지완료 -> 처리상태가 완료일 때 모집인상태값이 바뀔 수 있다.
	private String plStat;			//처리상태							-> [MAS001]미요청,승인요청,변경요청,해지요청,보완요청(=반려),취소,완료
	@ExcelColumn(headerName = "모집인등록번호", order = 9)
	private String plRegistNo;		//모집인등록번호							-> 은행연합회에서 던져주는 정보
	private String ci;				//CI값
	@ExcelColumn(headerName = "이름", order = 4)
	private String plMName;			//모집인이름
	@ExcelColumn(headerName = "주민번호", order = 5)
	private String plMZId;			//모집인주민등록번호
	private String plMZIdEdu;		//교육수료대조(모집인주민등록번호 대체값)
	@ExcelColumn(headerName = "휴대폰번호", order = 6)
	private String plCellphone;		//모집인휴대전화번호
	private String addr;			//본점소재지						-> [ADD001]1~17
	private String addrDetail;		//상세주소(법인등기부등본상)
	private String plEduNo;			//교육이수번호
	private String careerStartDate;	//경력시작일
	private String careerEndDate;	//경력종료일 
	private String entrustDate;		//위탁예정일
	private String comContDate;		//계약일자
	@ExcelColumn(headerName = "법인명", order = 7)
	private String plMerchantName;	//법인명
	private String plCeoName;		//대표이사명
	@ExcelColumn(headerName = "법인번호", order = 8)
	private String plMerchantNo;	//법인등록번호
	private String plBusinessNo;	//사업자등록번호
	private String compPhoneNo;		//회원사대표번호
	private String corpFoundDate;	//법인설립일
	private int capital;			//자본금
	private String plPayStat;		//결제상태							-> []
	private String comRegDate;		//신청일
	@ExcelColumn(headerName = "승인완료일", order = 10)
	private String creAppDate;		//승인일
	@ExcelColumn(headerName = "자격취득일", order = 12)
	private String creLicenseDate;	//자격취득일
	@ExcelColumn(headerName = "결제완료일", order = 11)
	private String payDate;			//결제완료일
	private String sendMsg;			//전달메세지
	private String plHistCd;		//해지사유코드
	private String plHistTxt;		//직접입력(사유)
	private String comHaejiDate;	//회원사해지일자
	private String creHaejiDate;	//협회해지일자
	private Integer fileSeq;		//첨부파일시퀀스
	
	//가공
	private int[] masterSeqArr;
	private String comCodeNm;		//담당회원사명
	private String memberNm;		//담당자명
	private String email;			//담당자이메일
	private String mobileNo;		//담당자휴대폰번호
	@ExcelColumn(headerName = "모집인분류", order = 2)
	private String plClassNm;		//분류명
	private String careerTypNm;		//구분명
	@ExcelColumn(headerName = "금융상품유형", order = 3)
	private String plProductNm;		//취급상품명
	@ExcelColumn(headerName = "모집인상태", order = 0)
	private String plRegStatNm;		//모집인상태명
	@ExcelColumn(headerName = "처리상태", order = 1)
	private String plStatNm;		//처리상태명
	private String addrNm;			//주소명
	private String plPayStatNm;		//결제상태명
	private String fileCompYn;		//첨부상태							-> Y,N
	private String fileCompTxt;		//첨부상태							-> 완료,미완료
	private String plHistCdNm;		//해지사유코드명
	
	private String originCreAppDate;
	private String originPayDate;
	private String originCreLicenseDate;
	private String originRegTimestamp;
	private String originUpdTimestamp;
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//개인,법인 첨부파일 가공
	private FileDomain fileType1;
	private FileDomain fileType2;
	private FileDomain fileType3;
	private FileDomain fileType4;
	private FileDomain fileType5;
	private FileDomain fileType6;
	private FileDomain fileType7;
	private FileDomain fileType8;
	private FileDomain fileType9;
	
	//법인 > 기타 첨부파일 가공
	private FileDomain fileType21;
	private FileDomain fileType22;
	private FileDomain fileType23;
	private FileDomain fileType24;
	private FileDomain fileType25;
	private FileDomain fileType26;
	
	//검색
	private String srchInput1;
	private String srchSelect1;
	private String srchSelect2;
	private String srchDate1;
	private String srchDate2;
}
