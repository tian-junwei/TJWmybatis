package com.tianjunwei.page.dialect;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

public class Sqlserver2012Dialect extends Dialect{

	public Sqlserver2012Dialect(MappedStatement mappedStatement,
			Object parameterObject, RowBounds pageBounds) {
		super(mappedStatement, parameterObject, pageBounds);
	}

}
