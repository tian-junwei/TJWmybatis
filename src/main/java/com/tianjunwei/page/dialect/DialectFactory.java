package com.tianjunwei.page.dialect;


public class DialectFactory {

	
	private static String dialectClass = null;
	
	public static String getDialectClass(String dataBaseType) throws Exception{
		/*if(databaseProductName.toLowerCase().indexOf("mysql") != -1 
      			&& dataBaseType.toLowerCase().indexOf("mysql") != -1){
      		dialectClass = MysqlDialect.class.getName();
      	}else if(databaseProductName.toLowerCase().indexOf("oracle") != -1 
      			&& dataBaseType.toLowerCase().indexOf("oracle") != -1){
      		dialectClass = OracleDialect.class.getName();
      	}else if (databaseProductName.toLowerCase().indexOf("postgresql") != -1
      			&& dataBaseType.toLowerCase().indexOf("postgresql") != -1) {
      		dialectClass = PostgresqlDialect.class.getName();
      	}else if(dataBaseType.toLowerCase().indexOf("sqlserver2005") != -1){
      		dialectClass = Sqlserver2005Dialect.class.getName();
      	}else if(dataBaseType.toLowerCase().indexOf("sqlserver2008") != -1){
      		dialectClass = Sqlserver2008Dialect.class.getName();
      	}else if(dataBaseType.toLowerCase().indexOf("sqlserver2012") != -1){
      		dialectClass = Sqlserver2012Dialect.class.getName();	
      	}else {
			throw new Exception("no support "+databaseProductName+" dialect");
		}*/
		
		dialectClass = "com.tianjunwei.page.dialect."+dataBaseType.substring(0, 1).toUpperCase() + dataBaseType.substring(1).toLowerCase()+"Dialect";
		return dialectClass;
	}
}
