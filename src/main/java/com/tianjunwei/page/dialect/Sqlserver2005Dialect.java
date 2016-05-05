package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class Sqlserver2005Dialect extends Dialect {

	public Sqlserver2005Dialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

}
