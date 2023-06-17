package com.fly.robot.exception;

import cn.hutool.core.util.StrUtil;
import com.fly.robot.api.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Set;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //参数校验异常处理器
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult handleValidException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.iterator().next().getMessage();

        return CommonResult.failed(errorMessage);
    }

    //参数校验异常处理器
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }
    //权限异常处理器
    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult handleValidException(AccessDeniedException e) {
        return CommonResult.validateFailed(e.getMessage());
    }

}
