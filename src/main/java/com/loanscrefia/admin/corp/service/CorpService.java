package com.loanscrefia.admin.corp.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.admin.corp.domain.CorpDomain;
import com.loanscrefia.admin.corp.repository.CorpRepository;
import com.loanscrefia.config.message.ResponseMsg;
import com.loanscrefia.member.user.domain.UserDomain;

import sinsiway.CryptoUtil;

@Service
public class CorpService {

	@Autowired private CorpRepository corpRepo;
	
	//암호화 적용여부
	@Value("${crypto.apply}")
	public boolean cryptoApply;
	
	//법인 리스트
	@Transactional(readOnly=true)
	public List<CorpDomain> selectCorpList(CorpDomain corpDomain) {
		
		//검색어 암호화
		if(StringUtils.isNotEmpty(corpDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				corpDomain.setPlMerchantNo(CryptoUtil.encrypt(corpDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				corpDomain.setPlMerchantNo(corpDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//리스트
		List<CorpDomain> corp 	= corpRepo.selectCorpList(corpDomain);
		
		//리스트 데이터 복호화
		String plMerchantNo 	= "";
		for(int i=0; i<corp.size(); i++) {
			if(cryptoApply) {
				plMerchantNo = CryptoUtil.decrypt(corp.get(i).getPlMerchantNo());
			}else {
				plMerchantNo = corp.get(i).getPlMerchantNo();
			}
			plMerchantNo = plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
			corp.get(i).setPlMerchantNo(plMerchantNo);
		}
		
		return corp;
	}
	
	//법인 저장
	@Transactional
	public ResponseMsg saveCorpInfo(CorpDomain corpDomain) {
		
		//법인번호 암호화
		if(StringUtils.isNotEmpty(corpDomain.getPlMerchantNo())) {
			if(cryptoApply) {
				corpDomain.setPlMerchantNo(CryptoUtil.encrypt(corpDomain.getPlMerchantNo().replaceAll("-", "")));
			}else {
				corpDomain.setPlMerchantNo(corpDomain.getPlMerchantNo().replaceAll("-", ""));
			}
		}
		
		//법인번호 중복체크
		int count = this.plMerchantNoCheck(corpDomain);
		if(count > 0) {
			return new ResponseMsg(HttpStatus.OK, "fail", 0, "해당 법인등록번호가 이미 등록되어 있습니다.");
		}

		//저장
		int result = 0;
		if(corpDomain.getCorpSeq() == null) {
			//등록
			result = corpRepo.insertCorpInfo(corpDomain);
		}else {
			//수정
			result = corpRepo.updateCorpInfo(corpDomain);
		}
		
		//결과
		if(result > 0) {
			return new ResponseMsg(HttpStatus.OK, "success", 1, "저장되었습니다.");
		}
		return new ResponseMsg(HttpStatus.OK, "fail", 0, "");
	}
	
	//법인 저장 : 엑셀 업로드
	@Transactional
	public void insertCorpInfoByExcel(CorpDomain corpDomain) {
		
		List<Map<String, Object>> excelParam 	= corpDomain.getExcelParam();
		
		for(int i = 0;i < excelParam.size();i++) {
			CorpDomain chkParam = new CorpDomain();
			String itemA 		= (String)excelParam.get(i).get("A"); //법인명
			String itemE 		= (String)excelParam.get(i).get("E"); //법인번호(암호화된 상태)

			//중복체크
			chkParam.setPlMerchantNo(itemE);
			int chkResult = corpRepo.selectCorpInfoCnt(chkParam);
			
			//결과
			if(chkResult == 0) {
				chkParam.setPlMerchantName(itemA);
				chkParam.setPathTyp("1");
				chkParam.setPassYn("N");
				corpRepo.insertCorpInfo(chkParam);
		 	}
		}
	}
	
	//법인 상세
	@Transactional(readOnly=true)
	public CorpDomain getCorpInfo(CorpDomain corpDomain) {

		CorpDomain result 	= corpRepo.getCorpInfo(corpDomain);
		String plMerchantNo	= "";
		
		if(cryptoApply) {
			plMerchantNo = CryptoUtil.decrypt(result.getPlMerchantNo());
		}else {
			plMerchantNo = result.getPlMerchantNo();
		}
		
		plMerchantNo = plMerchantNo.substring(0, 6) + "-" + plMerchantNo.substring(6);
		result.setPlMerchantNo(plMerchantNo);
		
		return result;
	}

	//법인등록번호 중복체크
	@Transactional(readOnly = true)
	public int plMerchantNoCheck(CorpDomain corpDomain) {
		return corpRepo.plMerchantNoCheck(corpDomain);
	}

	//법인등록번호 사용여부 체크 
	@Transactional(readOnly = true)
	public int plMerchantNoSearchCheck(CorpDomain corpDomain) {
		return corpRepo.plMerchantNoSearchCheck(corpDomain);
	}
	
	//법인삭제
	@Transactional
	public ResponseMsg deleteCorpInfo(CorpDomain corpDomain) {
		int[] corpSeqArr = corpDomain.getCorpSeqArr();
		if(corpSeqArr.length > 0) {
			int searchCnt = 0;
			int resultCnt = 0;
			for(int tmp : corpSeqArr) {
				CorpDomain search = new CorpDomain();
				search.setCorpSeq(tmp);
				searchCnt = corpRepo.plMerchantNoSearchCheck(search);
				resultCnt = resultCnt+searchCnt;
			}
			
			if(resultCnt > 0) {
				return new ResponseMsg(HttpStatus.OK, "fail", 0, "사용중인 법인번호가 존재합니다.");
			}else {
				corpRepo.deleteCorpInfo(corpDomain);
				return new ResponseMsg(HttpStatus.OK, "success", 1, "삭제되었습니다.");
			}
		}else {
			return new ResponseMsg(HttpStatus.OK, "fail", 0, "오류가 발생하였습니다.");
		}
	}
	
	//법인 금융감독원 승인여부 체크
	@Transactional(readOnly = true)
	public int corpPassCheck(UserDomain userDomain) {
		return corpRepo.corpPassCheck(userDomain);
	}
	
	@Transactional
	public ResponseMsg insertCheckCorp(CorpDomain corpDomain) {
		
		//리스트
		List<CorpDomain> corp = corpRepo.selectCheckCorpList(corpDomain);
		
		for(CorpDomain tmp : corp) {
			CorpDomain resultDomain = new CorpDomain();
			resultDomain.setPlMerchantName(tmp.getPlMerchantName());
			
			if(cryptoApply) {
				resultDomain.setPlMerchantNo(CryptoUtil.encrypt(tmp.getPlMerchantNo().replaceAll("-", "")));
			}else {
				resultDomain.setPlMerchantNo(tmp.getPlMerchantNo().replaceAll("-", ""));
			}
			
			resultDomain.setPathTyp("2");
			resultDomain.setPassYn("Y");
			resultDomain.setRegSeq(1);
			resultDomain.setUpdSeq(1);
			corpRepo.insertCorpInfo(resultDomain);
		}
		return new ResponseMsg(HttpStatus.OK, "success", 1, "저장되었습니다.");
	}
}
