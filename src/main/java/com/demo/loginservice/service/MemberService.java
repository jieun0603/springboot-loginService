package com.demo.loginservice.service;

import com.demo.loginservice.domain.Member;
import com.demo.loginservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     *
     * @param member
     * @return
     */
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복회원검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복회원검증
     *
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회
     *
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 이메일로 회원조회
     * 
     * @param memberEmail
     * @return
     */
    public Optional<Member> findOne(String memberEmail) {
        return memberRepository.findByEmail(memberEmail);
    }

    /**
     * 로그인
     * 
     * @param member
     * @return
     */
    public Optional<Member> login(Member member) {

        return memberRepository.login(member);
    }
}
