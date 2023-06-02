package com.matching.member.dto;

import com.matching.member.domain.Member;
import com.matching.member.domain.MemberSkill;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String email;
    private String name;
    private String organization;
    private String job;
    private String profileImageUrl;
    private String accessToken;
    List<String> memberSkillsId = new ArrayList<>();

    public static MemberResponse of(Member member, String accessToken) {
        List<String> skillList = fromSkillList(member.getMemberSkills());

        return MemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .organization(member.getOrganization())
                .job(member.getJob())
                .profileImageUrl(member.getProfileImageUrl())
                .accessToken(accessToken)
                .memberSkillsId(skillList)
                .build();
    }

    public static List<String> fromSkillList(List<MemberSkill> memberSkills) {
        return memberSkills.stream()
                .map(list -> list.getSkill().getName())
                .collect(Collectors.toList());
    }
}
