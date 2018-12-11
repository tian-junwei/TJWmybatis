package com.tianjunwei.main;

import com.tianjunwei.main.entity.User;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @author tianjunwei
 * @date 2018/12/11 17:12
 */

public class PageMain {


    public static void main(String[] args) {
        //mybatis的配置文件
        String resource = "mybatis-config.xml";
        InputStream is = PageMain.class.getClassLoader().getResourceAsStream(resource);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        List<User> users = session.selectList("com.tianjunwei.main.entity.User.page", null, new RowBounds(2, 5));
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}