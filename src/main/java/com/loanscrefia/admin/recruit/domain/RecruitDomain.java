package com.loanscrefia.admin.recruit.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.front.search.domain.SearchResultDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("recruit")
public class RecruitDomain extends BaseDomain {

	//모집인정보(tb_lc_mas01) & 모집인정보이력(tb_lc_mas01_hist)
	private int masterSeq;			//시퀀스
	private int masterHistSeq;		//이력시퀀스
	@ExcelColumn(headerName = "접수번호", order = 0)
	private String masterToId;		//접수번호ID(조회)
	
	private String regPath;			//등록경로(BO:B, FO:F)
	
	private String comCode;			//담당회원사코드
	private int memberSeq;			//담당자시퀀스
	private String plClass;			//분류							-> [CLS001]개인,법인
	private String plWork;			//업종
	@ExcelColumn(headerName = "법인사용인여부", order = 6)
	private String corpUserYn;		//법인사용인여부
	private String careerTyp;		//구분							-> [CAR001]신규,경력
	
	private String apiCareerYn;		//은행연합회API 경력여부 조회결과값
	
	private String plProduct;		//금융상품유형(취급상품)				-> [PRD001]대출,리스,TM대출,TM리스
	private String plRegStat;		//모집인상태						-> [REG001]승인전,승인완료,자격취득,해지완료 -> 처리상태가 완료일 때 모집인상태값이 바뀔 수 있다.
	private String plStat;			//처리상태							-> [MAS001]미요청,승인요청,변경요청,해지요청,보완요청(=반려),변경요청(보완),해지요청(보완),취소,완료
	private String oldPlStat;		//현재처리상태
	@ExcelColumn(headerName = "등록번호", order = 1)
	private String plRegistNo;		//모집인등록번호						-> 은행연합회에서 던져주는 정보(1:n)
	@ExcelColumn(headerName = "계약번호", order = 18)
	private String conNum;			//계약번호							-> 은행연합회에서 던져주는 정보(1:1)
	@ExcelColumn(headerName = "CI", order = 17)
	private String ci;				//CI값
	@ExcelColumn(headerName = "이름", order = 8)
	private String plMName;			//모집인이름
	@ExcelColumn(headerName = "주민번호", order = 9)
	private String plMZId;			//모집인주민등록번호
	private String plMZIdEnc;		//모집인주민등록번호암호화비교
	private String plMZIdEdu;		//교육수료대조(모집인주민등록번호 대체값)
	@ExcelColumn(headerName = "휴대폰번호", order = 10)
	private String plCellphone;		//모집인휴대전화번호
	private String addr;			//본점소재지						-> [ADD001]1~17
	private String addrDetail;		//상세주소(법인등기부등본상)
	private String plEduNo;			//교육이수번호
	private String careerStartDate;	//경력시작일
	private String careerEndDate;	//경력종료일 
	private String entrustDate;		//위탁예정일
	@ExcelColumn(headerName = "법인명", order = 11)
	private String plMerchantName;	//법인명
	private String plCeoName;		//대표이사명
	@ExcelColumn(headerName = "법인번호", order = 12)
	private String plMerchantNo;	//법인등록번호
	private String plBusinessNo;	//사업자등록번호
	private String compPhoneNo;		//회원사대표번호
	private String corpFoundDate;	//법인설립일
	private String capital;			//자본금
	private String votingStockCnt;	//의결권있는주식총수
	private String comContDate;		//계약일자(회원사)
	private String comRegDate;		//신청일(심사요청일)
	private String chkYn;			//실무자확인여부
	private String adminChkYn;		//관리자확인여부
	@ExcelColumn(headerName = "승인완료일", order = 13)
	private String creAppDate;		//승인일
	
	private String firstAppDate;	//최초승인요청일
	
	@ExcelColumn(headerName = "자격취득일", order = 15)
	private String creLicenseDate;	//자격취득일
	private String plPayStat;		//결제상태							-> []
	private String payDate;			//결제완료일
	private String sendMsg;			//전달메세지
	private String plHistCd;		//해지사유코드
	private String plHistTxt;		//직접입력(사유)
	private String applyHistTxt;	//승인요청사유
	private String comHaejiDate;	//회원사해지일자						-> 회원사가 해지요청한 날짜를 넣어줌
	private String creHaejiDate;	//협회해지일자						-> 회원사가 해지요청한 날짜를 넣어줌
	private Integer fileSeq;		//첨부파일시퀀스
	private int fileGrpSeq;
	
	//모집인단계별이력(tb_lc_mas01_step)
	private int masterStepSeq;
	
	//가공
	private int[] masterSeqArr;
	
	@ExcelColumn(headerName = "회원사", order = 2)
	private String comCodeNm;		//담당회원사명
	private String memberNm;		//담당자명
	private String email;			//담당자이메일
	private String mobileNo;		//담당자휴대폰번호
	private String extensionNo;		//담당자내선번호
	@ExcelColumn(headerName = "모집인분류", order = 5)
	private String plClassNm;		//분류명
	private String careerTypNm;		//구분명
	@ExcelColumn(headerName = "금융상품유형", order = 7)
	private String plProductNm;		//취급상품명
	@ExcelColumn(headerName = "모집인상태", order = 3)
	private String plRegStatNm;		//모집인상태명
	@ExcelColumn(headerName = "처리상태", order = 4)
	private String plStatNm;		//처리상태명
	private String addrNm;			//주소명
	private String plPayStatNm;		//결제상태명
	private String fileCompYn;		//첨부상태							-> Y,N
	private String fileCompTxt;		//첨부상태							-> 완료,미완료
	private String plHistCdNm;		//해지사유코드명
	
	private String histPlMName;
	private String histPlMZId;
	private String histPlCellphone;
	
	private String histType;
	
	private String originPlMerchantNo;
	private String originCreAppDate;
	private String originPayDate;
	private String originCreLicenseDate;
	private String originRegTimestamp;
	private String originUpdTimestamp;
	
	private String creGrp;
	
	private String payType;
	private String payName;
	private String originPayRegDate;
	@ExcelColumn(headerName = "결제완료일", order = 14)
	private String payRegDate;
	
	private String afterData;		//변경이력 현재데이터
	
	private String updFileCheck;	//변경정보에 대한 리스트 조회용 필드
	
	private String preRegYn;		//기등록여부
	private String subYn;			//약식등록여부
	@ExcelColumn(headerName = "가등록번호", order = 16)
	private String preLcNum;		//가등록번호
	private String preRegState;		//가등록상태
	
	private String searchPlMName;
	private String searchPlMZId;
	private String searchPlCellphone;
	
	private int histNameSeq;
	private int histZidSeq;
	private int histPhoneSeq;
	
	
	private List<SearchResultDomain> SearchResultList;	// FO - 은행연합회 API검색결과 필드
	
	private String searchPayDate;	// 결제일 정렬 btn값 필드
	private String orderPayDate;	// 결제일 정렬 필드
	
	
	// 2021-10-14 컬럼추가
	private String otherField;			// 영위하는 다른업종
	private String withinGovr;			// 관할검찰청 또는 지청
	private String regAddr;				// 등록기준지
	private String regAddrDetail;		// 등록기준지 상세
	private String addrBase;			// 주소
	
	
	//엑셀 업로드
	private List<Map<String, Object>> excelParam;
	
	//사용여부
	private String useYn;
	
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
	private FileDomain fileType10;
	private FileDomain fileType11;
	private FileDomain fileType12;
	private FileDomain fileType13;
	private FileDomain fileType14;
	private FileDomain fileType15;
	
	private FileDomain fileType31;
	private FileDomain fileType32;
	
	//법인 > 기타 첨부파일 가공
	private FileDomain fileType21;
	private FileDomain fileType22;
	private FileDomain fileType23;
	private FileDomain fileType24;
	private FileDomain fileType25;
	private FileDomain fileType26;
	private FileDomain fileType27;
	private FileDomain fileType28;
	private FileDomain fileType29;
	
	//검색
	private String srchInput1;
	private String srchSelect1;
	private String srchSelect2;
	private String srchDate1;
	private String srchDate2;
	
	
	
	//변경사항
	private FileDomain histFileType1;
	private FileDomain histFileType2;
	private FileDomain histFileType3;
	private FileDomain histFileType4;
	private FileDomain histFileType5;
	private FileDomain histFileType6;
	private FileDomain histFileType7;
	private FileDomain histFileType8;
	private FileDomain histFileType9;
	private FileDomain histFileType10;
	private FileDomain histFileType11;
	private FileDomain histFileType12;
	private FileDomain histFileType13;
	private FileDomain histFileType14;
	private FileDomain histFileType15;
	private FileDomain histFileType31;
	private FileDomain histFileType32;
	
	
	
	private FileDomain histFileType21;
	private FileDomain histFileType22;
	private FileDomain histFileType23;
	private FileDomain histFileType24;
	private FileDomain histFileType25;
	private FileDomain histFileType26;
	
	private FileDomain histFileType27;
	private FileDomain histFileType28;
	private FileDomain histFileType29;
	
	private Integer userSeq;
}
