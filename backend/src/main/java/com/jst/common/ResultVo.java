package com.jst.common;
import lombok.Data;

@Data
public class ResultVo {
    String message;
    String code;
    Object object;

    public ResultVo(String code, String message, Object object){
        this.code=code;
        this.message=message;
        this.object=object;
    }

    public ResultVo(String code, String message){
        this(code,message,null);
    }
}
