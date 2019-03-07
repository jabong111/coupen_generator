package com.mycom.first;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {
	
	@Resource(name="memberService")
	private MemberService memberService;
	
	ModelAndView mav = new ModelAndView();
	
	//로그인폼 메서드
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}
	
	//로그인
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView memberLogin(HttpServletRequest request, MemberModel member) {
		MemberModel result =  memberService.memberLogin(member);
		
		if(result == null) {	//입력한 아이디와 비밀번호가 디비에 저장되어있는 아이디 비밀번호와 일치하지 않을때
			mav.setViewName("loginError");
			return mav;


		}
		if(result.getNAME().equals("admin")) {	//어드민 로그인
			HttpSession session = request.getSession();
			
			session.setAttribute("member",result );
			session.setAttribute("name",result.getNAME());
			mav.setViewName("couponGen");
			return mav;
			
		}else{
			HttpSession session = request.getSession();
			
			session.setAttribute("member", result);
			session.setAttribute("name", result.getNAME());
			
			mav.setViewName("couponUse");
			return mav;
		}		
	}
	



}
