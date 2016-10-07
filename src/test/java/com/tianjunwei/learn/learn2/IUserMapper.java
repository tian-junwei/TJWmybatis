package com.tianjunwei.learn.learn2;

import org.apache.ibatis.annotations.Select;

import com.tianjunwei.learn.learn1.entity.User;

public interface IUserMapper {

	@Select("select * from users where id=#{id}")
	public User getById(int id);
}
