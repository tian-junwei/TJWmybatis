package com.tianjunwei.sqlperformance;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;


/**
 *  mybatis插件，实现打印mybatis执行的sql语句执行时间，用于监控sql语句执行的性能
 */

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class SqlPerformanceInterceptor implements Interceptor {
	
	public static final Map<String, SqlEntity> sqlMap=new ConcurrentHashMap<String, SqlEntity>();
	public String mapperIds ="";
	
	public Object intercept(Invocation invocation) throws Throwable {
		//获得参数中的第一个，包含了我们需要的Configuration信息
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		//获得sql语句的参数
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		//获得对应mapper文件中的id
		String sqlId = mappedStatement.getId();
		//包含了sql语句信息
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		long startTime = System.currentTimeMillis();
		Object returnValue = null;
		returnValue = invocation.proceed();
		long endTime = System.currentTimeMillis();
		String[] mapperId = mapperIds.split(",");
		if(mapperIds.length() < 1 || mapperId.length < 1 ){
			SqlEntity sql = getSql(configuration, boundSql, sqlId, startTime,endTime);
			sqlMap.put(sqlId, sql);
		}else{
			for(int i = 0; i < mapperId.length ; i++){
				if(sqlId.startsWith(mapperId[i])){
					SqlEntity sql = getSql(configuration, boundSql, sqlId, startTime,endTime);
					sqlMap.put(sqlId, sql);
					break;
				}
			}
		}
		return returnValue;
	}

	/***
	 * 获得sql语句执行的性能信息
	 */
	public static SqlEntity getSql(Configuration configuration, BoundSql boundSql, String sqlId, long startTime, long endTime) {
		
		String sqlString = showSql(configuration, boundSql);
		SqlEntity sql = sqlMap.get(sqlId);
		long executorTime = endTime - startTime;
		if(sql == null){
			sql = new SqlEntity();
			sql.setSqlId(sqlId);
			sql.setSql(sqlString);
			sql.setTotal(1);
			sql.setTotalTime(executorTime);
			sql.setMaxTime(executorTime);
		}else {
			if(sql.getMaxTime() < executorTime){
				sql.setMaxTime(executorTime);
				sql.setSql(sqlString);
			}
			int total = sql.getTotal();
			long totalTime = sql.getTotalTime() + executorTime ;
			sql.setTotalTime(totalTime);
			sql.setTotal(total+1);
		}
		
		return sql;
	}

	/***
	 * 获得参数信息
	 */
	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(obj) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}

	/***
	 * 将参数和sql语句组装起来
	 */
	public static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			//将sql语句中的占位符替换为参数
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = StringUtils.replaceOnce(sql, "?", getParameterValue(parameterObject));
				//sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = StringUtils.replaceOnce(sql, "?", getParameterValue(obj));
						//sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = StringUtils.replaceOnce(sql, "?", getParameterValue(obj));
						//sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * @Title: setProperties
	 * @Description: 
	 * @param properties 
	 */ 
	@Override
	public void setProperties(Properties properties) {
		if(properties.containsKey("module")){
			mapperIds = properties.getProperty("module").trim();
			Pattern p = Pattern.compile("\t|\r|\n");
		    Matcher m = p.matcher(mapperIds);
		    mapperIds = m.replaceAll("");
		    mapperIds = mapperIds.replaceAll(" +", "");
		}
	}
	
}