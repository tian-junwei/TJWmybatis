package com.tianjunwei.learn.learn2;

import java.io.InputStream;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.learn.learn1.Learn1Main;
import com.tianjunwei.learn.learn1.entity.User;

public class Learn2Main {
	
	public static void main(String [] args){
		
		String resource = "learn/mybatis-config.xml";
	    InputStream is = Learn1Main.class.getClassLoader().getResourceAsStream(resource);
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    IUserMapper userMapper = session.getMapper(IUserMapper.class);
	    User user = userMapper.getById(1);
	    user.getAge();
	    user.getId();
	    user.getNames();
	    /*RowBounds rowBounds = new RowBounds(2, 4);//接口分页编程
	    System.out.println(userMapper.page(rowBounds).size());*/
	}
}
