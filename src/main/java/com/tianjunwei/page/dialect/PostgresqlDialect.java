package com.tianjunwei.page.dialect;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;



/**
 * @ClassName: PostgreSQLDialect.java
 * @Description: postgresql 分页方言类
 * @author tianjunwei
 * @date 2016年1月13日下午3:09:28
 * @modify by user: tianjunwei
 * @modify by reason:
 * @version V1.0
 */
public class PostgresqlDialect extends AbstractDialect {

    public PostgresqlDialect(MappedStatement mappedStatement, Object parameterObject, RowBounds pageBounds) {
        super(mappedStatement, parameterObject, pageBounds);
    }

    /***
     * @Title: getLimitString
     * @Description: 组装分页sql语句
     * @param sql
     * @param offsetName
     * @param offset
     * @param limitName
     * @param limit
     * @return
     */
    protected String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {
        StringBuffer buffer = new StringBuffer( sql.length()+20 ).append(sql);
        if(offset > 0){
            buffer.append(" limit ? offset ?");
            setPageParameter(limitName, limit, Integer.class);
            setPageParameter(offsetName, offset, Integer.class);
        }else{
            buffer.append(" limit ?");
            setPageParameter(limitName, limit, Integer.class);
        }
		return buffer.toString();
	}
}
