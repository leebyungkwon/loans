package com.loanscrefia.common.common.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanscrefia.common.common.domain.TransactionDomain;
import com.loanscrefia.common.common.repository.CommonRepository;

@Service
public class TransactionService {

	@Autowired private CommonRepository commonRepository;
	
	@Transactional
	public void logSave(HttpServletRequest request) {
		
		
		String ip 			= request.getRemoteAddr();
		String url 			= request.getRequestURI();
		String ips 			= request.getHeader("X-Forwarded-For");
		
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
		data.setIp(ip);
		data.setIp2(ips);
		data.setParam(resultTxt);

		commonRepository.saveTransactionLog(data);
		
	}
}
