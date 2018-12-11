package com.tianjunwei.page.dialect;


public class DialectFactory {

	
	private static String dialectClass = null;
	
	public static String getDialectClass(String dataBaseType) throws Exception{
		dialectClass = "com.tianjunwei.page.dialect."+dataBaseType.substring(0, 1).toUpperCase() + dataBaseType.substring(1).toLowerCase()+"Dialect";
		return dialectClass;
	}
}
