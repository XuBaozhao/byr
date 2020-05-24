package com.connecttoes.connect.utils;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ResultUtil implements Serializable {
    /**
     *操作成功
     */
    public static final int SUCCESS = 200;
    /**
     * 操作失败
     */
    public static final int FAILED = 500;
    /**
     * 参数校验失败
     */
    public static final int VALIDATE_FAILED = 404;
    /**
     * 未认证
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 未授权
     */
    public static final int FORBIDDEN = 403;
    private int code;
    private String message;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 普通成功返回
     *
     */
    public ResultUtil success() {
        this.code = SUCCESS;
        this.message = "操作成功";
        return this;
    }

    /**
     * 普通成功返回
     *
     * @param data  获取的待返回的数据
     * @return
     */
    public ResultUtil success(Object data){
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = data;
        return this;
    }

    /**
     * 返回分页成功数据
     * @param data
     * @return
     */
    public ResultUtil pageSuccess(Page data){
        Map<String, Object> result = new HashMap<>(5);
        /*
        此处添加result的信息
         */
        result.put("totleElements",data.getTotalElements());
        result.put("totlePages", data.getTotalPages());
        result.put("data", data.getContent());
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = result;
        return this;
    }
    /**
     * 普通失败提示信息
     */
    public ResultUtil failed() {
        this.code = FAILED;
        this.message = "操作失败";
        return this;
    }
    /**
     * 普通失败提示信息
     */
    public ResultUtil failed(String message) {
        this.code = FAILED;
        this.message = message;
        return this;
    }
    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public ResultUtil validateFailed(String message) {
        this.code = VALIDATE_FAILED;
        this.message = message;
        return this;
    }
    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public ResultUtil unauthorized(String message) {
        this.code = UNAUTHORIZED;
        this.message = "暂未登录或token已经过期";
        this.data = message;
        return this;
    }
    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public ResultUtil forbidden(String message) {
        this.code = FORBIDDEN;
        this.message = "没有相关权限";
        this.data = message;
        return this;
    }
    /**
     * 参数验证失败使用
     *
     * @param result 错误信息
     */
    public ResultUtil validateFailed(BindingResult result) {
        validateFailed(result.getFieldError().getDefaultMessage());
        return this;
    }

}
