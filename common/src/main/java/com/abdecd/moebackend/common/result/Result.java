package com.abdecd.moebackend.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class Result<T> extends ResponseEntity<Result.ResultBody<T>> {

    /**
     * @code 编码：200成功, 其它数字为失败
     * @msg 错误信息
     * @data 数据
     */
    @SuppressWarnings("all")
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ResultBody<T> {
        private int code = 200;
        private String msg = "";
        private T data;
    }

    /**
     * http状态码与响应体相同
     */
    public Result(ResultBody<T> body) {
        super(body, HttpStatusCode.valueOf(body.getCode()));
    }

    public Result(ResultBody<T> body, int code) {
        super(body, HttpStatusCode.valueOf(code));
    }

//    public ResponseEntity<Result<T>> getResponseEntity() {
//        return ResponseEntity.status(this.code).body(this);
//    }

    public static Result<String> success() {
        var result = new ResultBody<String>();
        result.setData("ok");
        return new Result<>(result);
    }

    public static <T> Result<T> success(T object) {
        var result = new ResultBody<T>();
        result.setData(object);
        return new Result<>(result);
    }

    public static Result<String> error(String msg) {
        var result = new ResultBody<String>();
        result.setMsg(msg);
        result.setCode(400);
        return new Result<>(result);
    }

    public static Result<String> error(int code, String msg) {
        var result = new ResultBody<String>();
        result.setMsg(msg);
        result.setCode(code);
        return new Result<>(result);
    }
}
