package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//로직을 처리하다가 에러 발생 시 변경된 데이터를 로직을 수행하기 이전 상태로 콜백
@Transactional
//final이나 @NonNull이 붙은 필드에 생성자 생성.
@RequiredArgsConstructor
//MemberService가 UserDetailService 구현
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validatDuplicateMember(member);
        return memberRepository.save(member);
    }

//    이미 가입된 회원의 경우, 예외 발생
    private void validatDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }

    @Override
//    UserDetailsService 인터페이스의 loadUserByUsername() 메소드 오버라이딩.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

//        UserDetail을 구현하고 있는 User 객체 반환
        return User.builder()
                .username(member.getEmail()).password(member.getPassword()).roles(member.getRole().toString()).build();
    }

}
