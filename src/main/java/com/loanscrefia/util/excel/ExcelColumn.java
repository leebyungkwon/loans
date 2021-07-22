package com.loanscrefia.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
	String headerName() default "";
	String vCell() default "";
	String vEnum() default "";
	String chkDb() default "";
	String vEncrypt() default "";
	String chkPrd() default "";
	String chkFormat() default "";
	String chkDate() default "";
	int vLenMin() default 0;
	int vLenMax() default 10000;
	
	int order() default 0;
}