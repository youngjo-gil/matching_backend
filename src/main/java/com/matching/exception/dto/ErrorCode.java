package com.matching.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // OK
    SUCCESS(HttpStatus.OK, "OK"),

    // BAD REQUEST
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),
    USER_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 존재합니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 게시글이 없습니다."),

    PLAN_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 목표가 존재하지 않습니다.");


    private final HttpStatus status;
    private final String message;
}
