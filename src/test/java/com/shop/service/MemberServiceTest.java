package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//테스트 실행 후 롤백 처리를 해주는 메소드. 같은 메소드를 반복적으로 테스트 할 수 있다.
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //    Member Entity 생성
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("김민주");
        memberFormDto.setAddress("인천광역시 남동구 용현동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
//    Junit의 asserEquals 메소드를 이용하여 요청값과 실제 저장 데이터를 비교
//    assertEquals(기대 값, 실제 저장된 값);
    public void saveMemberTest(){

        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }
    @Test
    @DisplayName("중복 회원가입 테스트")
//    Junit의 asserEquals 메소드를 이용하여 요청값과 실제 저장 데이터를 비교
//    assertEquals(기대 값, 실제 저장된 값);
    public void saveDuplicateMemberTest(){

        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

//        예외 처리 테스트.
//        assertThrows(발생할 예외 타입, )
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

//        발생한 예외 메세지가 예상 결과와 맞는지 검증
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}