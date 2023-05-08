package com.matching.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    private Long userId;

    private String refreshToken;

    @Builder
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

}
