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
	    //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
	    InputStream is = Learn1Main.class.getClassLoader().getResourceAsStream(resource);
	    //构建sqlSession的工厂
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
	    //Reader reader = Resources.getResourceAsReader(resource); 
	    //构建sqlSession的工厂
	    //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
	    //创建能执行映射文件中sql的sqlSession
	    SqlSession session = sessionFactory.openSession();
	    /**
	     * 映射sql的标识字符串，
	     * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
	     * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
	     */
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
