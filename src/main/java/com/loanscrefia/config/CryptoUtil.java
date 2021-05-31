package com.loanscrefia.config;
import java.io.File;

import sinsiway.PcaSession;
import sinsiway.PcaSessionPool;


public class CryptoUtil {
	private static PcaSession session;
    
	private static final String initpath = "C:\\Program Files\\sinsiway\\petra\\api\\petra_cipher_api.conf";
	private static final String modeEncrypt = "(mode=(crypt=encrypt)(header_flag=on)(bypass_check=on)(force_target_write=on))(key=(1=(name=DEV.FILE_LOAN)(columns=1)))";
	private static final String modeDecrypt = "(mode=(crypt=decrypt)(header_flag=on)(bypass_check=on)(force_target_write=on))(key=(1=(name=DEV.FILE_LOAN)(columns=1)))";

	static {
		init();
	}

	 public static void init() {
		try {
			System.out.println("PcaSessionPool Initialize.....");
			PcaSessionPool.initialize(initpath,"");
			session = PcaSessionPool.getSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public static String encrypt(String s) {
		String result = null;
		try {
			if ( session == null ) session = PcaSessionPool.getSession();
			if ( s == null || "".equals(s) )
				result = "";
			else 
				result = session.encrypt("DEV.LOAN",s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String decrypt(String s) {
		String result = null;
		try {
			if ( session == null ) session = PcaSessionPool.getSession();
			if ( s == null || "".equals(s) )
				result = "";
			else 
				result = session.decrypt("DEV.LOAN",s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void encryptFile(String source){
		try{
			if(session == null)session = PcaSessionPool.getSession();
			if(source == null || "".equals(source)){
				Exception e = new Exception("Source File not found!");
				throw e;
			}else {
				try{
					try{
						String target = source+"_enc";
						session.cryptFile(modeEncrypt, source, target);
						File sFile = new File(source);
						File tFile = new File(target);
						sFile.delete();
						tFile.renameTo(sFile);
					}catch(Exception e){
						e.printStackTrace();
					} 
	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public static void encryptFile(String source, String target){
		try{
			if ( session == null ) session = PcaSessionPool.getSession();
			if ( source == null || "".equals(source) ){
				Exception e = new Exception("Source File not found!");
				throw e;				
			}else if ( target == null || "".equals(target) ){
				Exception e = new Exception("Target File not found!");
				throw e;
			}else {
					try{
						session.cryptFile(modeEncrypt, source, target);
					}catch (Exception e) {						
						e.printStackTrace();
					}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void decryptFile(String source, String target){
		try{
			if ( session == null ) session = PcaSessionPool.getSession();
			if ( source == null || "".equals(source) ){
				Exception e = new Exception("Source File not found!");
				throw e;				
			}else if ( target == null || "".equals(target) ){
				Exception e = new Exception("Target File not found!");
				throw e;
			}else {				
				try{
					session.cryptFile(modeDecrypt, source, target);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

