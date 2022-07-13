package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
//생성자를 자동으로 생성해주는 어노테이션
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
//    검증하려는 객체 앞에 @Vaild 어노테이션 선언, bindingResult 객체 추가.
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

//        에러가 있다면 회원가입 페이지로 이동
        if (bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
//            중복 회원 가입 예외 발생 시 에러 메세지와 함께 회원가입 창으로 이동
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }


//        회원가입 후 메인 페이지로 갈 수 있도록 함
        return "redirect:/";

    }

    @GetMapping(value = "/login")
    public String longinMember(){
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }
}
