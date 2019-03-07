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
	
	//�α����� �޼���
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}
	
	//�α���
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView memberLogin(HttpServletRequest request, MemberModel member) {
		MemberModel result =  memberService.memberLogin(member);
		
		if(result == null) {	//�Է��� ���̵�� ��й�ȣ�� ��� ����Ǿ��ִ� ���̵� ��й�ȣ�� ��ġ���� ������
			mav.setViewName("loginError");
			return mav;


		}
		if(result.getNAME().equals("admin")) {	//���� �α���
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
