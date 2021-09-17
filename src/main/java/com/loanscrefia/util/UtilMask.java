package com.loanscrefia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilMask {

	private final static Pattern ssn_pattern = Pattern.compile("^(\\d{6}\\D?\\d{1})(\\d{6})$");

	public static String maskSSN(String ssn) {
		Matcher matcher = ssn_pattern.matcher(ssn);
		if (matcher.find()) {
			return new StringBuffer(matcher.group(1)).append("******").toString();
		}
		return ssn;
	}

}
