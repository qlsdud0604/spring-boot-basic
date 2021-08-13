package com.example.springboot.repository;

import com.example.springboot.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();   // Optional에서 값을 꺼낼 때는 get() 메서드 사용
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName() {
        Member member01 = new Member();
        member01.setName("spring01");
        repository.save(member01);

        Member member02 = new Member();
        member02.setName("spring02");
        repository.save(member02);

        Member result = repository.findByName(member01.getName()).get();

        assertThat(result).isEqualTo(member01);
    }

    @Test
    public void findAll() {
        Member member01 = new Member();
        member01.setName("spring01");
        repository.save(member01);

        Member member02 = new Member();
        member02.setName("spring02");
        repository.save(member02);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
