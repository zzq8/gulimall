package com.zzq.gulimall.product.exception;

import com.zzq.common.exception.BizCodeEnum;
import com.zzq.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice("com.zzq.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class) //处理对应异常
    public R handlerValidException(MethodArgumentNotValidException e /*感知异常*/){
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item) -> {
            errorMap.put(item.getField(),item.getDefaultMessage());
        });
      return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(),BizCodeEnum.VAILD_EXCEPTION.getMessage()).put("data",errorMap);
    }

    //如果不能精确匹配异常，就来到最大的类 Exception
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){

        log.error("错误：",throwable);
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMessage());
    }
}
