package com.matching.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 존재합니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    FOLLOWING_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팔로잉할 유저를 찾을 수 없습니다."),
    CANNOT_FOLLOWING_SELF(HttpStatus.BAD_REQUEST, "본인을 팔로잉할 수 없습니다."),

    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한정보가 없는 토큰입니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다."),

    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 목표가 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 없습니다."),

    PARTICIPATE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 프로젝트 참여자가 아닙니다."),
    PARTICIPATE_NOT_LEADER(HttpStatus.BAD_REQUEST, "해당 프로젝트 리더가 아닙니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다.");


    private final HttpStatus status;
    private final String message;
}
