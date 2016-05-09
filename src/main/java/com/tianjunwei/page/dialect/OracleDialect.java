package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class OracleDialect  extends Dialect{

	public OracleDialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	/**   
	 *  
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
		// TODO Auto-generated method stub
		return null;
	}

}
