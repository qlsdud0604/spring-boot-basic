package com.example.springboot.config;

import com.example.springboot.repository.MemberRepository;
import com.example.springboot.repository.MemoryMemberRepository;
import com.example.springboot.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
