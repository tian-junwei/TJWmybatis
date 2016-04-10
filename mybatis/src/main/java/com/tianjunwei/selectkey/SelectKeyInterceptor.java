/** 
*@ProjectName: mybatis 
*@FileName: SelectKey.java 
*@Date: 2016年4月10日 
*@Copyright: 2016 tianjunwei All rights reserved. 
*/
package com.tianjunwei.selectkey;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: SelectKey.java
 * @Package com.tianjunwei.selectkey
 * @Description: 用于统一实现mybatis的配置文件中的selectKey的配置
 * @author tianjunwei tiantianjunwei@126.com
 * @date 2016年4月10日 上午12:10:26
 * @version V1.0
 */
@Intercepts({ @Signature(type = Executor.class,
		// 拦截Executor类的query方法
		method = "query",
		// 拦截器用于MappedStatement，Object，RowBounds和ResultHandler类上
		args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class SelectKeyInterceptor implements Interceptor {

	private static Logger logger = LoggerFactory.getLogger(SelectKeyInterceptor.class);

	/**
	 * @param invocation
	 * @return
	 * @throws Throwable
	 *             2016年3月30日
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		final Executor executor = (Executor) invocation.getTarget();
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[0];
		String sqlId = ms.getId();
		if (sqlId.endsWith(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
			Object parameter = null;
			// 获得sql语句的参数
			if (invocation.getArgs().length > 1) {
				parameter = invocation.getArgs()[1];
			}
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql();
			// 获得数据库类型来选择执行key生成器
			Connection connection = executor.getTransaction().getConnection();
			DatabaseMetaData databaseMetaData = null;
			if (connection != null) {
				databaseMetaData = connection.getMetaData();
			} else {
				logger.error("connection is null");
				throw new Exception("connection is null");
			}
			String databaseProductName = databaseMetaData.getDatabaseProductName();
			queryArgs[0] = createMappedStatement(ms, sql, databaseProductName);
			return invocation.proceed();
		} else {
			return invocation.proceed();
		}

	}

	private MappedStatement createMappedStatement(MappedStatement ms, String sql, String databaseProductName) {

		String keysql = "select nextval('swdf_id_seq'::regclass)";
		if (databaseProductName.equalsIgnoreCase("postgresql")) {
			keysql = "select nextval('" + sql + "'::regclass)";
		} else if (databaseProductName.equalsIgnoreCase("oracle")) {
			keysql = "select " + sql + " from dual";
		} else if (databaseProductName.equalsIgnoreCase("mysql")) {
			keysql = "select " + sql;
		}
		// 生成新的MappedStatement
		SqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), keysql);
		Builder builder = new Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());
		builder.resultMaps(ms.getResultMaps());
		return builder.build();

	}

	/**
	 * @param target
	 * @return 2016年3月30日
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * @param properties
	 *            2016年3月30日
	 */
	@Override
	public void setProperties(Properties properties) {

	}

}
