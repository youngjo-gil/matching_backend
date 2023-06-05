package com.matching.exception.dto;

import com.matching.exception.util.CustomException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class CustomErrorResponse {
    private final HttpStatus status;
    private final String code;
    private final String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public static ResponseEntity<CustomErrorResponse> error(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(CustomErrorResponse.builder()
                    .status(e.getErrorCode().getStatus())
                    .code(e.getErrorCode().name())
                    .message(e.getErrorCode().getMessage())
                    .build());
    }

}
