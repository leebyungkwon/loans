package com.loanscrefia.config.message;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseMsg {

    private int status;
    private String message;
    private Object data;
    private String code;
    
    private Long logId;
    
    public ResponseMsg() {}
    
	
    public ResponseMsg(HttpStatus status, String code) {
        this.status = status.value();
        this.code = code;
        this.data = null;
    }
    
    public ResponseMsg(HttpStatus status, String code, String message) {
        this.status = status.value();
        this.message = message;
        this.code = code;
        this.data = null;
    }
    public ResponseMsg(HttpStatus status, String code, Object data) {
        this.status = status.value();
        this.code = code;
        this.data = data;
    }
    public ResponseMsg(HttpStatus status, String code, Object data, String message) {
        this.status = status.value();
        this.code = code;
        this.message = message;
        this.data = data;
    }


}
