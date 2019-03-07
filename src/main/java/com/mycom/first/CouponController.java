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

	//�������� �ϴ� �޼���
	@RequestMapping(value = "couponGen.do")
	public String couponGen(HttpServletRequest request, String prefix, String group) {	

		ArrayList<CouponModel> couponList = new ArrayList<CouponModel>();	//������ ����� ��̸���Ʈ

		CouponModel result = couponService.couponGroupCheck(group.toUpperCase());	

		// �׷쿡 ������ �ִ°��̹Ƿ� �����޽����� ����.
		if (result != null) {
			return "couponGenError";
		}

		int cnt = 0;
		while (cnt < 10000) {
			String coupon = prefix.toUpperCase();	//����3�ڸ��� �빮�ڷ� �ٲ��ش�
			CouponModel aa = new CouponModel();

			for (int i = 0; i < 13; i++) {		//���� 3�ڸ� ���� 13���� �������� ���� 16�ڸ��� ����ũ�� ������ �����
				double charrandom = Math.random();
				int charValue = (int) (charrandom * (90 - 65 + 1)) + 65;	//���ĺ� �빮�� ASCII�ڵ������ ������ ������ �����

				double intrandom = Math.random();
				int intValue = (int) (intrandom * (57 - 48 + 1)) + 48;	//���� ASCII�ڵ������ ������ ������ �����

				double ran = Math.random();
				int ranValue = (int) (ran * (3 - 1 + 1)) + 1;	//���ĺ��� ���ڸ� �����ϰ� ��ġ�ϱ����� ���������� �����

				if (ranValue == 1) {	
					coupon += (char) intValue;	//���ڸ� �ִ´�.	

				} else {
					coupon += (char) charValue;	//���ĺ����ִ´�.
				}
			}

			if (couponList.contains(coupon)) {	//�����ϰ� ������� ������ �ߺ��Ǵ��� üũ�Ѵ�.
				continue;
			}
			aa.setCGROUP(group.toUpperCase());	
			aa.setCOUPON(coupon);
			aa.setUSEDATE(" ");
			aa.setUSEUSER(" ");
			aa.setISUSE(0);

			couponList.add(aa);		//�ߺ����� �ʴ´ٸ� ��̸���Ʈ�� �ִ´�.
			cnt++;

		}

		for (CouponModel couponModel : couponList) {
			couponService.couponGen(couponModel);		//������� ������ ��� �ִ´�.
		}

		return "couponGen";
	}
	
	//������� �޼���
	@RequestMapping(value="couponUse.do")
	public String couponUse(HttpServletRequest request, CouponModel couponModel) {

		CouponModel check = couponService.couponUseCheck(couponModel);	//������ ����� �� �ִ��� ������ ���� �������� �ƴ��� ��񿡼� ���� �����´�.

		if(check == null) {			//��� �������� ������ �������� �ʴ� ����
			return "noCoupon";
		}else {
			if(check.getUSEUSER().equals(" ")) {	//��� �����ϰ� ����� ����ڰ� �������� ������ ��밡���� �����̹Ƿ� ����Ѵ�.
				HttpSession session = request.getSession();
				String id = (String)session.getAttribute("name");
				couponModel.setUSEUSER(id);

				couponService.couponUse(couponModel);
				
				return "finishUse";
			}
		}


		
		return "aleadyUse";		//��� ���ǿ� �������� ���ϸ� �̹� ���������
	}
	
	//���� ����Ʈ �޼���
	@RequestMapping(value="couponList.do")
	public ModelAndView couponList(HttpServletRequest request, CouponModel couponModel) throws UnsupportedEncodingException {
		
		if(request.getParameter("currentPage") == null || request.getParameter("currentPage").trim().isEmpty() || request.getParameter("currentPage").equals("0")) {
			currentPage = 1;
		}else {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		List<CouponModel> result;
		
		String isSearch = request.getParameter("isSearch");

		if(isSearch != null) {	//�˻������ �����ϸ�
			isSearch = new String(isSearch.getBytes("8859_1"),"UTF-8");
			isSearch = isSearch.toUpperCase();
			
			result = couponService.couponSearchList(isSearch);
			totalCount = result.size();
			page = new Paging(currentPage, totalCount, blockCount, blockPage, "couponList",isSearch);
		}else {	//�˻������ �������� ������
			result = couponService.couponList();
			totalCount = result.size();
			page = new Paging(currentPage, totalCount, blockCount, blockPage, "couponList");
		}
		
		pagingHtml = page.getPagingHtml().toString();
		
		int lastCount = totalCount;
		
		if(page.getEndCount() < totalCount) {		//������������ �Ѿ�� ����������
			lastCount = page.getEndCount()+1;
		}
		
		result = result.subList(page.getStartCount(), lastCount);	//100���� �ڸ���
		

		mav.addObject("isSearch", isSearch);	
		mav.addObject("totalCount",totalCount);
		mav.addObject("result",result);
		mav.addObject("pagingHtml",pagingHtml);
		mav.addObject("currentPage",currentPage);
		mav.setViewName("couponList");

		return mav;
	}
	
	//���� ��볻�� ��� �޼���
	@RequestMapping(value="couponStat.do")
	public ModelAndView souponStat(HttpServletRequest request, CouponModel couponModel) {

		List<Map<String, Object>> result = couponService.couponStat();

		
		mav.addObject("result",result);
		mav.setViewName("couponStat");

		return mav;
	}
	

}



























