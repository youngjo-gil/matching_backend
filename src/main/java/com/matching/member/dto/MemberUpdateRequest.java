package com.matching.member.dto;

import com.matching.member.domain.MemberSkill;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequest {
    @NotNull
    private String password;
    private String profileImageUrl;
    private String organization;
    private String job;
    private List<Long> memberSkillsId = new ArrayList<>();
}