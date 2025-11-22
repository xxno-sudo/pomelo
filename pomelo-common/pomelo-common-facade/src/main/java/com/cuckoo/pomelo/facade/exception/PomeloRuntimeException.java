package com.cuckoo.pomelo.facade.exception;


import com.cuckoo.pomelo.facade.response.ResponseState;

public class PomeloRuntimeException extends RuntimeException {

    protected String code = ResponseState.ERROR.getCode();
    protected String message = ResponseState.ERROR.getName();

    public PomeloRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public PomeloRuntimeException(Throwable cause) {
        super(cause);
    }

    public PomeloRuntimeException() {
    }

    public PomeloRuntimeException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
