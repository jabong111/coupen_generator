package com.mycom.first;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CouponDao {
	
	//��������
	public void couponGen(CouponModel couponModel); 
	
	//�����׷��ִ��� üũ
	public CouponModel couponGroupCheck(String group);

	//��������Ʈ
	public List<CouponModel> couponList();
	
	//��������Ʈ�˻�
	public List<CouponModel> couponSearchList(String isSearch);

	//�����ڵ���
	public void couponUse(CouponModel couponModel);
	
	//�������üũ
	public CouponModel couponUseCheck(CouponModel couponModel);
	
	//�������
	public List<Map<String, Object>> couponStat();
	

}
