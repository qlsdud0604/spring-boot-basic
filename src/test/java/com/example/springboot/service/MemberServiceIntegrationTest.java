package com.example.springboot.service;


import com.example.springboot.domain.Member;
import com.example.springboot.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("spring");

        Long saveId = memberService.join(member);

        Member findMember = memberRepository.findById(saveId).get();

        assertEquals(member.getId(), findMember.getId());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        Member member01 = new Member();
        member01.setName("spring");

        Member member02 = new Member();
        member02.setName("spring");

        memberService.join(member01);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member02));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
