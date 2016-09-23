/** 
*@ProjectName: TTRabbitMQ 
*@FileName: MybatisTest.java 
*@Date: 2016年4月14日 
*@Copyright: 2016 tianjunwei All rights reserved. 
*/  
package com.tianjunwei.log.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tianjunwei.log.dao.entity.Log;

/**    
* @Title: MybatisTest.java  
* @Package com.tianjunwei.mybatis  
* @Description: 测试mybatis配置 
* @author tianjunwei  tiantianjunwei@126.com   
* @date 2016年4月14日 下午11:59:06  
* @version V1.0    
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext_test.xml")
public class MybatisTest {
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Test
	public void save(){
		List<Log> list = new ArrayList<Log>();
		Log log = new Log();
		log.setLogInfo("logInfo");
		log.setLogType(1);
		list.add(log);
		Log log2 = new Log();
		log2.setLogInfo("logInfo");
		log2.setLogType(2);
		list.add(log2);
		System.err.println(sqlSessionTemplate.insert(Log.class.getName()+".save", list));
	}
	
	//@Test 
	public void get(){
		Log log = sqlSessionTemplate.selectOne(Log.class.getName()+".get");
		System.err.println(log.getLogInfo());
	}
	
	//@Test
	public void list(){
		RowBounds rowBounds = new RowBounds(1, 10);
		String typeString ="a";
		List<Log> list = sqlSessionTemplate.selectList(Log.class.getName()+".listOne", typeString, rowBounds);
		System.err.println(list.size());
	}
	
	//@Test
	public void list2(){
		RowBounds rowBounds = new RowBounds(1, 10);
		String []typeString ={"a","b","c"};
		List<Log> list = sqlSessionTemplate.selectList(Log.class.getName()+".list", typeString, rowBounds);
		System.err.println(list.size());
	}
	
	//@Test
	public void delete(){
		sqlSessionTemplate.delete(Log.class.getName()+".delete", 2);
	}

}
