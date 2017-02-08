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
package com.tianjunwei.selectKey;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.tianjunwei.selectKey.entity.User;

/**
 * @author tianjunwei
 * @time 2017 下午8:34:41
 */
public class SelectKeyMain {

	public static void main(String [] args){
		//mybatis的配置文件
	    String resource = "learn/mybatis-config.xml";
	    InputStream is = SelectKeyMain.class.getClassLoader().getResourceAsStream(resource);
	    //构建sqlSession的工厂
	    SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
	    SqlSession session = sessionFactory.openSession();
	    User user = new User();
	    user.setAge(10);
	    user.setNames("yao");
	    session.insert("com.tianjunwei.selectKey.entity.User.add", user);
	    session.commit();
	    System.err.println(user.getId());
	    session.insert("com.tianjunwei.selectKey.entity.User.insert", user);
	    session.commit();
	    System.err.println(user.getId());
	}
}
