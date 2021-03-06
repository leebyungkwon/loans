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
		
		// 2022-01-06 접속 브라우저 확인
		String userAgent	= request.getHeader("User-Agent").toUpperCase();
		
		Enumeration<String> paramNames1 = request.getParameterNames();
		String resultTxt = "";
		while (paramNames1.hasMoreElements()) {
			String key = (String) paramNames1.nextElement();  
			String value = request.getParameter(key);
			value = value.trim();
			resultTxt += key+"="+value+"&";
		}

		TransactionDomain data = new TransactionDomain();
		
		data.setUrl("["+userAgent+"] "+url);
		data.setIp(ips);
		data.setIp2(ip);
		data.setParam(resultTxt);

		commonRepository.saveTransactionLog(data);
	}
	
	@Transactional
	public boolean isVaildIp(HttpServletRequest request) {
		boolean flag = false;

		String ip = (request.getHeader("X-Forwarded-For") == null) ?
					"0" : request.getHeader("X-Forwarded-For");
		
		
		//주소 코드 리스트
		CodeDtlDomain codeDtlParam = new CodeDtlDomain();
		codeDtlParam.setCodeMstCd("RT0001");
		List<CodeDtlDomain> codeList = codeService.selectCodeDtlList(codeDtlParam);
		
		for(CodeDtlDomain code : codeList) {
			//log.error("## isVaildIp : {} , {} , {}, {}", ip, code.getCodeDtlNm(), ip.matches(code.getCodeDtlNm()+".*"), ip.contains(code.getCodeDtlNm()));
			if(ip.matches(code.getCodeDtlNm()+".*")) {
				flag = true;
			}
		}
		
		return flag;
	}
}
