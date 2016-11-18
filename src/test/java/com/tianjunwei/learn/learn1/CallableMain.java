package com.tianjunwei.learn.learn1;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.learn.learn1.entity.User;

public class CallableMain {

	public static void main(String [] args){
		
		//mybatis的配置文件
	    String resource = "learn/mybatis-config.xml";
	    InputStream is = CallableMain.class.getClassLoader().getResourceAsStream(resource);
	    //构建sqlSession的工厂
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    String statement = "com.tianjunwei.learn.learn1.entity.User.getUser";//映射sql的标识字符串
	    //执行查询返回一个唯一user对象的sql
	    User user = session.selectOne(statement, 1);
	    System.out.println(user);
	    
	    Map<String, Integer> parameterMap = new HashMap<String, Integer>();
	    parameterMap.put("age", 12);
	    parameterMap.put("user_count", -1);
	    session.selectOne("com.tianjunwei.learn.learn1.entity.User.count", parameterMap);
	    Integer result = parameterMap.get("user_count");
	    System.out.println(result);
	    
	}
}
