package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class Sqlserver2005Dialect extends Dialect {

	public Sqlserver2005Dialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	/**   
	 * <blockquote><pre>
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =1;
     * 结果：return "WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (order by i_id asc) as __row_number__,  * from table order by i_id asc) SELECT * FROM query WHERE __row_number__ > 1 AND __row_number__ <= 11 ORDER BY __row_number__";
     * 或者
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =0;
     * 结果：return "WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (order by i_id asc) as __row_number__,  * from table order by i_id asc) SELECT * FROM query WHERE __row_number__ > 0 AND __row_number__ <= 10 ORDER BY __row_number__";
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
		StringBuffer pagingBuilder = new StringBuffer();
		String orderby = getOrderByPart(sql);
		String distinctStr = "";
		String loweredString = sql.toLowerCase();
		String sqlPartString = sql;
		if (loweredString.trim().startsWith("select")) {
			int index = 6;
			if (loweredString.startsWith("select distinct")) {
				distinctStr = "DISTINCT ";
				index = 15;
			}
			sqlPartString = sqlPartString.substring(index);
		}
		pagingBuilder.append(sqlPartString);
		// if no ORDER BY is specified use fake ORDER BY field to avoid errors
		if (orderby == null || orderby.length() == 0) {
			orderby = "ORDER BY CURRENT_TIMESTAMP";
		}
		StringBuffer result = new StringBuffer();
		result.append("WITH query AS (SELECT ")
				.append(distinctStr)
				.append("TOP 100 PERCENT ")
				.append(" ROW_NUMBER() OVER (")
				.append(orderby)
				.append(") as __row_number__, ")
				.append(pagingBuilder)
				.append(") SELECT * FROM query WHERE __row_number__ > ? AND __row_number__ <= ?")
				.append(" ORDER BY __row_number__");
        setPageParameter(offsetName,offset,Integer.class);
        setPageParameter("__offsetEnd",offset+limit,Integer.class);
		return result.toString();
	}
	static String getOrderByPart(String sql) {
		String loweredString = sql.toLowerCase();
		int orderByIndex = loweredString.indexOf("order by");
		if (orderByIndex != -1) {
			// if we find a new "order by" then we need to ignore
			// the previous one since it was probably used for a subquery
			return sql.substring(orderByIndex);
		} else {
			return "";
		}
	}

}
