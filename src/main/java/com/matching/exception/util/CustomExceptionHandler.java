//package com.matching.exception.util;
//
//import com.matching.common.dto.ResponseDto;
//import com.matching.common.utils.ResponseUtil;
//import com.matching.exception.dto.CustomErrorResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class CustomExceptionHandler {
//    @ExceptionHandler(CustomException.class)
//    protected ResponseDto<ResponseEntity<CustomErrorResponse>> handleCustomException(CustomException e) {
//        return ResponseUtil.FAILURE("예외 발생", CustomErrorResponse.toEntity(e.getErrorCode()));
//    }
//}
