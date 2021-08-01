package com.demo.loginservice.repository;

import com.demo.loginservice.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> login(Member member);
    List<Member> findAll();
}
