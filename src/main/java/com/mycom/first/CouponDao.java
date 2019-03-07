package com.mycom.first;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CouponDao {
	
	//쿠폰생성
	public void couponGen(CouponModel couponModel); 
	
	//쿠폰그룹있는지 체크
	public CouponModel couponGroupCheck(String group);

	//쿠폰리스트
	public List<CouponModel> couponList();
	
	//쿠폰리스트검색
	public List<CouponModel> couponSearchList(String isSearch);

	//쿠폰코드사용
	public void couponUse(CouponModel couponModel);
	
	//쿠폰사용체크
	public CouponModel couponUseCheck(CouponModel couponModel);
	
	//쿠폰통계
	public List<Map<String, Object>> couponStat();
	

}
