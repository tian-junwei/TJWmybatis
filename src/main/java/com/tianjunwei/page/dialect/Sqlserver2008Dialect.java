package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class Sqlserver2008Dialect extends Dialect{
	
	private static final String ORDERBY = "order by";
	private static final String SELECT = "select";
	private static final String SELECT_DISTINCT = "select distinct";

	public Sqlserver2008Dialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

	/**   
	 * <blockquote><pre>
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =1;
     * 结果：return "WITH page_query AS (select ROW_NUMBER() OVER (order by i_id asc) as row_nr__, * from table ) SELECT * FROM page_query WHERE row_nr__ between 2 and 11";
     * 或者
     * 参数：sql = " select * from table order by i_id asc"; limit=10; offset =0;
     * 结果：return "select top 10 * from table order by i_id asc ";
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
		String newsql="";
		 if(offset == 0){
			 newsql= new StringBuffer( sql.length() + 8 )
						.append( sql )
						.insert( getAfterSelectInsertPoint( sql ), " top " + limit )
						.toString();
			 return newsql;
		    }else{
				int endIndex = offset + limit;
				StringBuilder sb = new StringBuilder(sql.replaceAll("\\s{2,}", " ").toLowerCase().trim());
				if(sb.indexOf(SELECT_DISTINCT)==sb.indexOf(SELECT)){
					throw new UnsupportedOperationException(getClass().getName()+".getLimitString(String queryString, int limit, int offset) unsupport key DISTINCT in query");
				}
				int orderByIndex = sb.indexOf(ORDERBY);
				//判断queryString中是否有order by 若没有则创建。
				CharSequence orderby = orderByIndex > 0 ? sb.subSequence(orderByIndex, sb.length()) : " order by CURRENT_TIMESTAMP";
				if(orderByIndex > 0){
					//原句中有orderby时将其提取至Over()内
					sb.delete(orderByIndex, orderByIndex + orderby.length());
				}
				int selectEndIndex = sb.indexOf(SELECT) + SELECT.length();
				//构建分页sql
				sb.insert(selectEndIndex, " ROW_NUMBER() OVER (" + orderby + ") as row_nr__,");
				sb.insert(0, "WITH page_query AS (").append(") SELECT * FROM page_query ");
				sb.append("WHERE row_nr__ between ").append("?").append(" and ").append("?");
				setPageParameter("_offset", offset+1, Integer.class);
				setPageParameter("endIndex", endIndex, Integer.class);
				return sb.toString();
		    }
	}
	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

}
