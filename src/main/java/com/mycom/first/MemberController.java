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
	
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView memberLogin(HttpServletRequest request, MemberModel member) {
		MemberModel result =  memberService.memberLogin(member);
		
		if(result == null) {
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
