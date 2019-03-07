package com.mycom.first;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements MemberDao {

	@Resource(name="sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public MemberModel memberLogin(MemberModel member) {
		return sqlSessionTemplate.selectOne("member.login",member);
	}
	

}
