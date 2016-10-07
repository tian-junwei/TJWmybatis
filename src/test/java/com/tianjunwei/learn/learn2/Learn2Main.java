package com.tianjunwei.learn.learn2;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.learn.learn1.Learn1Main;

public class Learn2Main {
	
	public static void main(String [] args){
		
		String resource = "learn/mybatis-config.xml";
	    InputStream is = Learn1Main.class.getClassLoader().getResourceAsStream(resource);
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    IUserMapper userMapper = session.getMapper(IUserMapper.class);
	    userMapper.getById(1);
	}
	
    
    
}
