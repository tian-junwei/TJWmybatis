package com.tianjunwei.learn.learn2;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.tianjunwei.learn.learn1.entity.User;

public interface IUserMapper {

	public User getById(int id);
	public  List<User> page(RowBounds rowBounds);
}
