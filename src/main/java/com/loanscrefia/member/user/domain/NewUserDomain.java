package com.loanscrefia.member.user.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.loanscrefia.common.common.domain.BaseDomain;
import com.loanscrefia.common.common.domain.FileDomain;
import com.loanscrefia.util.excel.ExcelColumn;

import lombok.Data;

@Data
@Alias("newUser")
public class NewUserDomain extends BaseDomain {

	private int masterSeq;			//시퀀스
	@ExcelColumn(headerName = "접수번호", order = 0)
	private String masterToId;		//접수번호ID(조회)
	private String regPath;			//등록경로(BO:'B', FO:'F')
	private int comCode;			//담당회원사코드
	private int memberSeq;			//담당자시퀀스
	private int userSeq;
	private String subYn;			//약식등록여부
	private String preRegYn;		//기등록여부						-> 기등록되어있으면 결제 따로 X
	private String preLcNum;		//가등록번호
	private String plClass;			//분류							-> [CLS001]개인,법인
	private String plWork;			//업종
	@ExcelColumn(headerName = "법인사용인여부", order = 2)
	private String corpUserYn;		//법인사용인여부
	private String careerTyp;		//구분							-> [CAR001]신규,경력
	private String apiCareerYn;		//은행연합회API 경력여부 조회결과값
	private String plProduct;		//금융상품유형(취급상품)				-> [PRD001]대출,리스,TM대출,TM리스
	private String plRegStat;		//모집인상태						-> [REG001]승인전,승인완료,자격취득,해지완료,결제완료 -> 처리상태가 완료일 때 모집인상태값이 바뀔 수 있다.
	private String plStat;			//처리상태							-> [MAS001]미요청,승인요청,변경요청,해지요청,보완요청(=반려),변경요청(보완),해지요청(보완),취소,완료,등록요건 불충족(=부적격),보완 미이행,등록수수료 미결제
	private String plRegistNo;		//모집인등록번호						-> 은행연합회에서 던져주는 정보(1:n)
	private String conNum;			//계약번호							-> 은행연합회에서 던져주는 정보(1:1)
	private String ci;				//CI값
	@ExcelColumn(headerName = "모집인이름", order = 4)
	private String plMName;			//모집인이름
	private String plMZId;			//모집인주민등록번호
	private String plMZIdEdu;		//교육수료대조(모집인주민등록번호 대체값)
	@ExcelColumn(headerName = "모집인휴대전화번호", order = 5)
	private String plCellphone;		//모집인휴대전화번호
	private String addr;			//본점소재지						-> [ADD001]1~17
	private String addrDetail;		//상세주소(법인등기부등본상)
	private String plEduNo;			//교육이수번호
	private String careerStartDate;	//경력시작일
	private String careerEndDate;	//경력종료일 
	private String entrustDate;		//위탁예정일
	@ExcelColumn(headerName = "법인명", order = 6)
	private String plMerchantName;	//법인명
	private String plCeoName;		//대표이사명
	@ExcelColumn(headerName = "법인등록번호", order = 7)
	private String plMerchantNo;	//법인등록번호
	private String plBusinessNo;	//사업자등록번호
	private String compPhoneNo;		//회원사대표번호
	private String corpFoundDate;	//법인설립일
	private String capital;			//자본금
	private String votingStockCnt;	//의결권있는 발행주식 총수
	@ExcelColumn(headerName = "계약기간", order = 8)
	private String comContDate;		//계약일자(회원사)
	@ExcelColumn(headerName = "승인요청일", order = 9)
	private String comRegDate;		//신청일(심사요청일)
	@ExcelColumn(headerName = "최초승인요청일", order = 10)
	private String firstAppDate;	//최초승인요청일
	private String chkYn;			//실무자확인여부
	private String creAppDate;		//승인일
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
	
	//모집인단계별이력(tb_lc_mas01_step)
	private int masterStepSeq;
	
	//위반이력정보(tb_lc_violation)
	private int violationSeq;
	private String violationCd;		//위반코드
	private String vioNum;			//위반이력번호						-> 은행연합회에서 던져주는 정보
	private String applyYn;			//위반이력삭제요청
	
	//배열
	private int[] masterSeqArr;
	private String[] violationCdArr;
	
	// 2021-10-14 컬럼추가
	private String otherField;			// 영위하는 다른업종
	private String withinGovr;			// 관할검찰청 또는 지청(법인)
	private String regAddr;				// 등록기준지
	private String regAddrDetail;		// 등록기준지 상세
	private String addrBase;			// 주소
	
	private String withinPolice;		// 관할경찰청
	private String withinPoliceNm;		// 관할경찰청명
	private String withinAdm;			// 관할행정기관
	private String withinAdmNm;			// 관할행정기관명
	private String withinAdmName;		// 관할행정기관직접입력
	
	
	
	//가공
	private String comCodeNm;		//담당회원사명
	private String memberNm;		//담당자명
	private String email;			//담당자이메일
	private String mobileNo;		//담당자휴대폰번호
	private String extensionNo;		//담당자내선번호
	@ExcelColumn(headerName = "모집인분류", order = 1)
	private String plClassNm;		//분류명
	private String careerTypNm;		//구분명
	@ExcelColumn(headerName = "취급상품명", order = 3)
	private String plProductNm;		//취급상품명
	private String plRegStatNm;		//모집인상태명
	@ExcelColumn(headerName = "상태", order = 11)
	private String plStatNm;		//처리상태명
	private String addrNm;			//주소명
	private String plPayStatNm;		//결제상태명
	private String fileCompYn;		//첨부상태							-> Y,N
	private String fileCompTxt;		//첨부상태							-> 완료,미완료
	private String imwonFileCompYn;
	private String expertFileCompYn;
	private String itFileCompYn;
	private String etcFileCompYn;
	private String plHistCdNm;		//해지사유코드명
	private String violationCdNm;	//위반코드명
	
	private String originPlMerchantNo;
	private String originCreAppDate;
	private String originPayDate;
	private String originCreLicenseDate;
	private String originRegTimestamp;
	private String originUpdTimestamp;
	
	private int imwonCnt;
	private int expertCnt;
	private int itCnt;
	private int imwonEduCnt;
	
	//결제
	private String payType;
	private String payName;
	private String originPayRegDate;
	private String payRegDate;
	
	private String apiResMsg;				// API결과메세지
	private String apiCode;					// API코드
	private String apiSuccessCode;			// API결과코드
	
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
	private FileDomain fileType29;
	private FileDomain fileType41;
	
	//검색
	private String srchInput1;
	private String srchSelect1;
	private String srchSelect2;
	private String srchDate1;
	private String srchDate2;
	
	//세션
	private String creGrp;
	
	// 2021-10-25 처리상태 요청자시퀀스, 처리상태 요청경로 추가
	private int plStatReqSeq;
	private String plStatReqPath;
	private String plStatReqPathNm;
	
	private String birthDt;
	private String gender;
	
}
