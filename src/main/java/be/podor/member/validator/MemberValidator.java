package be.podor.member.validator;

import be.podor.exception.podor.PodoalException;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;

public class MemberValidator {

    public static Member validate(MemberRepository memberRepository, Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> PodoalException.NO_MEMBER_EXCEPTION);
    }
}
