package com.mycom.first;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
public class CouponService implements CouponDao {
	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void couponGen(CouponModel couponModel) {
		sqlSessionTemplate.insert("coupon.insertCoupon",couponModel);
	}

	@Override
	public List<CouponModel> couponList() {
		return sqlSessionTemplate.selectList("coupon.selectCoupon");
	}

	@Override
	public List<CouponModel> couponSearchList(String isSearch) {
		return sqlSessionTemplate.selectList("coupon.selectSearchCoupon", isSearch);
	}

	@Override
	public void couponUse(CouponModel couponModel) {
		sqlSessionTemplate.insert("coupon.insertUse", couponModel);
		
	}

	@Override
	public CouponModel couponGroupCheck(String group) {
		return sqlSessionTemplate.selectOne("coupon.selectGroup", group);
	}

	@Override
	public CouponModel couponUseCheck(CouponModel couponModel) {
		return sqlSessionTemplate.selectOne("coupon.selectUse",couponModel);
	}

	@Override
	public List<Map<String, Object>> couponStat() {
		return sqlSessionTemplate.selectList("coupon.couponStat");
	}
	

}
