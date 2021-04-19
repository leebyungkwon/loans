package com.loanscrefia.util;

public class UtilString {
	public static boolean isStr(String str) {
		return str == null || str.equals("");
	}
	public static String setStr(String str) {
		return (isStr(str)) ?"":str;
	}
}