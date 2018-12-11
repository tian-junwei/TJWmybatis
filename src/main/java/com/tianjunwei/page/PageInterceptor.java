package com.tianjunwei.page;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.mapping.MappedStatement.Builder;

import com.tianjunwei.page.dialect.AbstractDialect;
import com.tianjunwei.page.dialect.DialectFactory;

@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor{
		static int MAPPED_STATEMENT_INDEX = 0;
		static int PARAMETER_INDEX = 1;
		static int ROWBOUNDS_INDEX = 2;
		static int RESULT_HANDLER_INDEX = 3;
	    String dialectClass;
	    boolean asyncTotalCount = false;
	    String dataBaseType=null;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public Object intercept(final Invocation invocation) throws Throwable {
	        final Executor executor = (Executor) invocation.getTarget();
	        final Object[] queryArgs = invocation.getArgs();
	        final MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
	        final Object parameter = queryArgs[PARAMETER_INDEX];
	        final RowBounds rowBounds = (RowBounds)queryArgs[ROWBOUNDS_INDEX];
	        //如果不需要分页操作，直接返回
	        if(rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET
	                && rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT){
	            return invocation.proceed();
	        }
	        //判断数据源选择方言，暂时支持mysql、oracle、postgresql和sql server 2005 2008及2012
	        Connection connection = executor.getTransaction().getConnection();
	        DatabaseMetaData databaseMetaData = null;
	        if(connection != null){
	        	databaseMetaData = connection.getMetaData();
	        }else {
				throw new Exception("connection is null");
			}
	         
	        String databaseProductName = databaseMetaData.getDatabaseProductName();
	        if( dataBaseType == null || "".equals(dataBaseType)){
	        	dataBaseType = databaseProductName;
	        }
	        //通过xml方言的配置来获得方言类
	        if(databaseProductName != null && !("".equals(dataBaseType))){
	        	
	        	dialectClass = DialectFactory.getDialectClass(dataBaseType);
	        
	        }else{
	        	throw new Exception("the property of dialect is null");
	        }
	      	setDialectClass(dialectClass);
	      	
	        final AbstractDialect dialect;
	        try {
	            Class clazz = Class.forName(dialectClass);
	            Constructor constructor = clazz.getConstructor(new Class[]{MappedStatement.class, Object.class, RowBounds.class});
	            dialect = (AbstractDialect)constructor.newInstance(new Object[]{ms, parameter, rowBounds});
	        
	        } catch (Exception e) {
	            throw new ClassNotFoundException("Cannot create dialect instance: "+dialectClass,e);
	        }
	        final BoundSql boundSql = ms.getBoundSql(parameter);
			//创建新的MappedStatement，此时的sql语句已经是符合数据库产品的分页语句
			//dialect.getPageSQL()获得分页语句
			//dialect.getParameterMappings(), dialect.getParameterObject(),添加了两个参数及其值，两个参数为_limit和_offset
	        queryArgs[MAPPED_STATEMENT_INDEX] = copyFromNewSql(ms,boundSql,dialect.getPageSQL(), dialect.getParameterMappings(), dialect.getParameterObject());
			//sql语句的参数集合
	        queryArgs[PARAMETER_INDEX] = dialect.getParameterObject();
			//设置为不分页，由新的sql语句进行物理分页
	        queryArgs[ROWBOUNDS_INDEX] = new RowBounds(RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT);
	        return invocation.proceed();
		}

		//创建新的MappedStatement
	    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql,
											   String sql, List<ParameterMapping> parameterMappings, Object parameter){
			//根据新的分页sql语句创建BoundSql
	        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql, parameterMappings, parameter);
	        //根据newBoundSql创建新的MappedStatement
	        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
	    }

		private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql,
				String sql, List<ParameterMapping> parameterMappings,Object parameter) {
			BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, parameterMappings, parameter);
			for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			    String prop = mapping.getProperty();
			    if (boundSql.hasAdditionalParameter(prop)) {
			        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			    }
			}
			return newBoundSql;
		}

		private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
			Builder builder = new Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
			
			builder.resource(ms.getResource());
			builder.fetchSize(ms.getFetchSize());
			builder.statementType(ms.getStatementType());
			builder.keyGenerator(ms.getKeyGenerator());
			if(ms.getKeyProperties() != null && ms.getKeyProperties().length !=0){
	            StringBuffer keyProperties = new StringBuffer();
	            for(String keyProperty : ms.getKeyProperties()){
	                keyProperties.append(keyProperty).append(",");
	            }
	            keyProperties.delete(keyProperties.length()-1, keyProperties.length());
				builder.keyProperty(keyProperties.toString());
			}
			//setStatementTimeout()
			builder.timeout(ms.getTimeout());
			//setStatementResultMap()
			builder.parameterMap(ms.getParameterMap());
			//setStatementResultMap()
	        builder.resultMaps(ms.getResultMaps());
			builder.resultSetType(ms.getResultSetType());
			//setStatementCache()
			builder.cache(ms.getCache());
			builder.flushCacheRequired(ms.isFlushCacheRequired());
			builder.useCache(ms.isUseCache());
			return builder.build();
		}

		public Object plugin(Object target) {
			return Plugin.wrap(target, this);
		}

		/**
		 * @Title: setProperties 
		 * @Description: 方言插件配置时设置的参数
		 * @param properties 参数
		 * @return  void  
		 * @2016年1月13日下午3:54:47
		 */
		public void setProperties(Properties properties) {
			dataBaseType = properties.getProperty("dialectType");
		}
		
		public static class BoundSqlSqlSource implements SqlSource {
			BoundSql boundSql;
			public BoundSqlSqlSource(BoundSql boundSql) {
				this.boundSql = boundSql;
			}
			public BoundSql getBoundSql(Object parameterObject) {
				return boundSql;
			}
		}

	    public void setDialectClass(String dialectClass) {
	        this.dialectClass = dialectClass;
	    }

	    public void setDataBaseType(String dataBaseType) {
			this.dataBaseType = dataBaseType;
		}
	    

}
