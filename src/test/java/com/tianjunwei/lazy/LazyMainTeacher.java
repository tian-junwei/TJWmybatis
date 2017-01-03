/**
 *    Copyright  2017  tianjunwei
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.tianjunwei.lazy;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.lazy.entity.Teacher;
import com.tianjunwei.lazy.entity.User;


/**
 * @author tianjunwei
 * @time 2017 上午10:24:21
 */
public class LazyMainTeacher {
public static void main(String [] args){
		
		//mybatis的配置文件
	    String resource = "learn/mybatis-config.xml";
	    InputStream is = LazyMainTeacher.class.getClassLoader().getResourceAsStream(resource);
	    //构建sqlSession的工厂
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    String statement = "com.tianjunwei.lazy.entity.Teacher.getTeacher";//映射sql的标识字符串
	    //执行查询返回一个唯一user对象的sql
	    Teacher teacher =  (Teacher) session.selectList(statement, 1).get(0);
	    session.commit(true);
	    System.out.println(teacher.getName());
	    List<User> users = teacher.getUsers();
	    System.err.println(users.get(3).getAge());
	    
	}
}
