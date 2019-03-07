package com.mycom.first;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CouponController {
	@Resource(name = "couponService")
	private CouponService couponService;
	
	private int currentPage = 1;
	private int totalCount;
	private int blockCount = 100;
	private int blockPage = 5;
	private String pagingHtml;
	private Paging page;

	ModelAndView mav = new ModelAndView();
	
	@ModelAttribute("couponModel")
	public CouponModel formBack() {
		return new CouponModel();	
	}

	//쿠폰생성 하는 메서드
	@RequestMapping(value = "couponGen.do")
	public String couponGen(HttpServletRequest request, String prefix, String group) {	

		ArrayList<CouponModel> couponList = new ArrayList<CouponModel>();	//쿠폰을 등록할 어레이리스트

		CouponModel result = couponService.couponGroupCheck(group.toUpperCase());	

		// 그룹에 쿠폰이 있는것이므로 에러메시지를 띄운다.
		if (result != null) {
			return "couponGenError";
		}

		int cnt = 0;
		while (cnt < 10000) {
			String coupon = prefix.toUpperCase();	//고정3자리를 대문자로 바꿔준다
			CouponModel aa = new CouponModel();

			for (int i = 0; i < 13; i++) {		//고정 3자리 이후 13개를 랜덤으로 만들어서 16자리의 유니크한 쿠폰을 만든다
				double charrandom = Math.random();
				int charValue = (int) (charrandom * (90 - 65 + 1)) + 65;	//알파벳 대문자 ASCII코드범위의 랜덤한 변수를 만든다

				double intrandom = Math.random();
				int intValue = (int) (intrandom * (57 - 48 + 1)) + 48;	//숫자 ASCII코드범위의 랜덤한 변수를 만든다

				double ran = Math.random();
				int ranValue = (int) (ran * (3 - 1 + 1)) + 1;	//알파벳과 숫자를 랜덤하게 배치하기위한 랜덤변수를 만든다

				if (ranValue == 1) {	
					coupon += (char) intValue;	//숫자를 넣는다.	

				} else {
					coupon += (char) charValue;	//알파벳을넣는다.
				}
			}

			if (couponList.contains(coupon)) {	//랜덤하게 만들어진 쿠폰이 중복되는지 체크한다.
				continue;
			}
			aa.setCGROUP(group.toUpperCase());	
			aa.setCOUPON(coupon);
			aa.setUSEDATE(" ");
			aa.setUSEUSER(" ");
			aa.setISUSE(0);

			couponList.add(aa);		//중복되지 않는다면 어레이리스트에 넣는다.
			cnt++;

		}

		for (CouponModel couponModel : couponList) {
			couponService.couponGen(couponModel);		//만들어진 쿠폰을 디비에 넣는다.
		}

		return "couponGen";
	}
	
	//쿠폰사용 메서드
	@RequestMapping(value="couponUse.do")
	public String couponUse(HttpServletRequest request, CouponModel couponModel) {

		CouponModel check = couponService.couponUseCheck(couponModel);	//쿠폰을 사용할 수 있는지 없는지 사용된 쿠폰인지 아닌지 디비에서 값을 가져온다.

		if(check == null) {			//디비에 존재하지 않으면 존재하지 않는 쿠폰
			return "noCoupon";
		}else {
			if(check.getUSEUSER().equals(" ")) {	//디비에 존재하고 사용한 사용자가 존재하지 않으면 사용가능한 쿠폰이므로 사용한다.
				HttpSession session = request.getSession();
				String id = (String)session.getAttribute("name");
				couponModel.setUSEUSER(id);

				couponService.couponUse(couponModel);
				
				return "finishUse";
			}
		}


		
		return "aleadyUse";		//모든 조건에 만족하지 못하면 이미 사용한쿠폰
	}
	
	//쿠폰 리스트 메서드
	@RequestMapping(value="couponList.do")
	public ModelAndView couponList(HttpServletRequest request, CouponModel couponModel) throws UnsupportedEncodingException {
		
		if(request.getParameter("currentPage") == null || request.getParameter("currentPage").trim().isEmpty() || request.getParameter("currentPage").equals("0")) {
			currentPage = 1;
		}else {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		List<CouponModel> result;
		
		String isSearch = request.getParameter("isSearch");

		if(isSearch != null) {	//검색결과가 존재하면
			isSearch = new String(isSearch.getBytes("8859_1"),"UTF-8");
			isSearch = isSearch.toUpperCase();
			
			result = couponService.couponSearchList(isSearch);
			totalCount = result.size();
			page = new Paging(currentPage, totalCount, blockCount, blockPage, "couponList",isSearch);
		}else {	//검색결과과 존재하지 않으면
			result = couponService.couponList();
			totalCount = result.size();
			page = new Paging(currentPage, totalCount, blockCount, blockPage, "couponList");
		}
		
		pagingHtml = page.getPagingHtml().toString();
		
		int lastCount = totalCount;
		
		if(page.getEndCount() < totalCount) {		//끝페이지에서 넘어갈때 다음페이지
			lastCount = page.getEndCount()+1;
		}
		
		result = result.subList(page.getStartCount(), lastCount);	//100개만 자르지
		

		mav.addObject("isSearch", isSearch);	
		mav.addObject("totalCount",totalCount);
		mav.addObject("result",result);
		mav.addObject("pagingHtml",pagingHtml);
		mav.addObject("currentPage",currentPage);
		mav.setViewName("couponList");

		return mav;
	}
	
	//쿠폰 사용내역 통계 메서드
	@RequestMapping(value="couponStat.do")
	public ModelAndView souponStat(HttpServletRequest request, CouponModel couponModel) {

		List<Map<String, Object>> result = couponService.couponStat();

		
		mav.addObject("result",result);
		mav.setViewName("couponStat");

		return mav;
	}
	

}



























