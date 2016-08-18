package com.tianjunwei.page.dialect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class OracleDialect  extends Dialect{

	public OracleDialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	/**   
	 * <blockquote><pre>
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =1;
     * 结果：return "select * from ( select row_.*, rownum rownum_ from ( select * from table order by i_id asc ) row_ ) where rownum_ <=11 and rownum_ > 1";
     * 或者
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =0;
     * 结果：return "select * from ( select * from table order by i_id asc ) where rownum <= 10";
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
		
		sql = sql.trim();
		boolean isForUpdate = false;
        Pattern p = Pattern.compile("\t|\r|\n");
        Matcher m = p.matcher(sql);
        sql = m.replaceAll(" ");
        sql = sql.replaceAll(" +", " ");
        
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}
		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			pagingSelect.append(" ) row_ ) where rownum_ <= ? and rownum_ > ?");
            setPageParameter("__offsetEnd",offset+limit,Integer.class);
            setPageParameter(offsetName,offset,Integer.class);
		}else {
			pagingSelect.append(" ) where rownum <= ?");
            setPageParameter(limitName,limit,Integer.class);
		}
		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}
		
		return pagingSelect.toString();
	}

}
