package com.demo.loginservice.controller;


import com.demo.loginservice.domain.Member;
import com.demo.loginservice.service.MemberService;
import com.demo.loginservice.util.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    /**
     * 회원가입
     * 
     * @param form
     * @return
     */
    @PostMapping("members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members/login")
    public String loginForm() {
        return "home";
    }

    /**
     *
     * 로그인 처리
     *
     * @param session
     * @param request
     * @param form
     * @param model
     * @return
     */
    /**
     *
     * @param session
     * @param request
     * @param response
     * @param form
     * @param model
     * @return
     */
    @PostMapping("members/login")
    public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response, MemberForm form, Model model) throws UnsupportedEncodingException {
        Member member = new Member();
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());

        Optional<Member> loginMember = memberService.login(member);

        SessionManager.clearSession(session);
        SessionManager.setLogin(session, member.getEmail());
        // 쿠키 설정(UID)
        setCookie(response, "UID", member.getEmail(), 60*60*24);

        return "members/memeberLogin";
    }

    /**
     *
     * 로그아웃
     *
     * @param session
     * @param response
     * @return
     */
    @PostMapping("member/logout")
    public String logout(HttpSession session, HttpServletResponse response) throws UnsupportedEncodingException {
        if(!StringUtils.isEmpty(SessionManager.getLoginId(session))) {
            setCookie(response, "UID", SessionManager.getLoginId(session), 0);
        }
        SessionManager.invalidateSession(session);
        return "home";
    }

    @GetMapping("members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    /**
     * 쿠키 설정
     *
     * @param response
     * @param key
     * @param value
     * @param age
     * @throws UnsupportedEncodingException
     */
    private void setCookie(HttpServletResponse response, String key, String value, int age) throws UnsupportedEncodingException {
        if(!StringUtils.isEmpty(value)) {
            value = value.replaceAll("\r", "").replaceAll("\n", "");
            value = URLEncoder.encode(value, "UTF-8");
            value = value.replaceAll("\\+", "%20");
        }

        CookieGenerator cg = new CookieGenerator();

        cg.setCookieName(key);
        if (age == 0) {
            cg.removeCookie(response);
        } else {
            // 쿠키 소멸 시간 설정(초단위) => 지정하지 않으면 웹브라우저 종료할 때 쿠키를 함께 삭제
            cg.setCookieMaxAge(age);
            // 응답헤더에 쿠키 추가하기
            cg.addCookie(response, value);
        }
    }

}
