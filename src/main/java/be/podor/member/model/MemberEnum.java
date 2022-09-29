package be.podor.member.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberEnum {
    ROLE_ADMIN,
    ROLE_MEMBER,
    ROLE_GUEST
}
