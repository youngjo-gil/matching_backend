package com.matching.exception.dto;

import com.matching.exception.util.CustomException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private HttpStatus status;
    private String code;
    private String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

//    public static ResponseEntity<CustomErrorResponse> toEntity(ErrorCode e) {
//        return ResponseEntity
//                .status(e.getStatus())
//                .body(CustomErrorResponse.builder()
//                    .status(e.getStatus())
//                    .code(e.name())
//                    .message(e.getMessage())
//                    .build());
//    }

}
