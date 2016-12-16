package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class Sqlserver2012Dialect extends Dialect{

	public Sqlserver2012Dialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	/**   
	 * <blockquote><pre>
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =1;
     * 结果：return "select * from table order by i_id asc offset 1 row fetch next 10 rows only";
     * 或者
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =0;
     * 结果：return "select * from table order by i_id asc offset 0 row fetch next 10 rows only";
     * </blockquote></pre> 
	 * @param sql
	 * @param offsetName
	 * @param offset
	 * @param limitName
	 * @param limit
	 * @return
	 * @see com.tianjunwei.page.dialect.Dialect#getLimitString(java.lang.String, java.lang.String, int, java.lang.String, int)   
	 */ 
	@Override
	protected String getLimitString(String sql, String offsetName, int offset, String limitName, int limit) {
		sql = new StringBuilder(sql)
	    		.append(" offset ")
	    		.append("?")
	    		.append(" row fetch next ")
	    		.append("?")
	    		.append(" rows only").toString();
		setPageParameter("_offset", offset, Integer.class);
		setPageParameter("_limit", limit, Integer.class);
		return sql;
	}

}
