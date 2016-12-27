package com.tianjunwei.learn.learn1;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.learn.learn1.entity.User;

public class Learn1Main {

	public static void main(String [] args){
		
		//mybatis的配置文件
	    String resource = "learn/mybatis-config.xml";
	    InputStream is = Learn1Main.class.getClassLoader().getResourceAsStream(resource);
	    //构建sqlSession的工厂
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    String statement = "com.tianjunwei.learn.learn1.entity.User.getUser";//映射sql的标识字符串
	    //执行查询返回一个唯一user对象的sql
	    User user = session.selectOne(statement, 1);
	    session.commit(true);
	    System.out.println(user.getNames());
	    /*SqlSession session2 = sessionFactory.openSession();
	    user = session2.selectOne(statement, 1);
	    System.out.println(user.getName());*/
	    
	}
}
