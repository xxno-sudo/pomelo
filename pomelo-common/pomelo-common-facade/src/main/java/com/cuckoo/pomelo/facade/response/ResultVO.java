package com.cuckoo.pomelo.facade.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
public class ResultVO {

    private String resCode;

    private String resMsg;

    private Object data;

    private ResultVO(String resCode, String resMsg, Object data) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.data = data;
    }

    public ResultVO() {
        this.resCode = ResponseState.SUCCESS.getCode();
        this.resMsg = ResponseState.SUCCESS.getName();
    }

    public ResultVO(String resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public static <T extends Serializable> ResultVO forSuccess(T data) {
        return new ResultVO(ResponseState.SUCCESS.getCode(), ResponseState.SUCCESS.getName(), data);
    }
}
