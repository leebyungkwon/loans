package com.loanscrefia.common.common.service;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.TransactionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;
import com.loanscrefia.system.code.domain.CodeDtlDomain;
import com.loanscrefia.system.code.service.CodeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EnterService {

	@Autowired private CommonRepository commonRepository;
	@Autowired private CodeService codeService;

	@Value("${spring.profiles.active}")
	private String profile;
	
	@Transactional
	public void logSave(HttpServletRequest request) {
		

		String ips 			= request.getHeader("X-Forwarded-For");
		String ip 			= request.getRemoteAddr();
		String url 			= request.getRequestURI();
		
		Enumeration<String> paramNames1 = request.getParameterNames();
		String resultTxt = "";
		while (paramNames1.hasMoreElements()) {
			String key = (String) paramNames1.nextElement();  
			String value = request.getParameter(key);
			value = value.trim();
			resultTxt += key+"="+value+"&";
		}

		TransactionDomain data = new TransactionDomain();
		
		data.setUrl(url);
		data.setIp(ips);
		data.setIp2(ip);
		data.setParam(resultTxt);

		commonRepository.saveTransactionLog(data);
	}
	
	@Transactional
	public boolean isVaildIp(HttpServletRequest request) {
		boolean flag = false;
		
		//if("local".equals(profile) || "dev".equals(profile)) return flag;
		
		String ip = (request.getHeader("X-Forwarded-For") == null) ?
					"0" : request.getHeader("X-Forwarded-For");
		//flag = (ip == "") ? true :  flag ;
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("RT0001");
		codeDtlParam.setCodeDtlNm(ip);
		List<CodeDtlDomain> codeList = codeService.selectCodeDtlList(codeDtlParam);
		
		for(CodeDtlDomain code : codeList) {
			log.info("## isVaildIp : {} , {}", ip, code.getCodeDtlNm());
			if(ip.indexOf(code.getCodeDtlNm()) >= 0) {
				flag = true;
			}
		}
		return flag;
	}
}
