/** 
*@ProjectName: mybatis 
*@FileName: Dialect.java 
*@Date: 2016��3��31�� 
*@Copyright: 2016 tianjunwei All rights reserved. 
*/  
package com.tianjunwei.page.dialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**    
* @Title: Dialect.java  
* @Package com.tianjunwei.page.dialect  
* @Description: 方言类
* @author tianjunwei  tiantianjunwei@126.com   
* @date 2016 年3 月31日 下午11:10:04  
* @version V1.0    
*/
public  class PageOperation {
    protected TypeHandlerRegistry typeHandlerRegistry;
    protected MappedStatement mappedStatement;
    protected RowBounds pageBounds;
    protected Object parameterObject;
    protected BoundSql boundSql;
    protected List<ParameterMapping> parameterMappings;
    protected Map<String, Object> pageParameters = new HashMap<String, Object>();
    private String pageSQL;
    private AbstractDialect dialect;

    
    public PageOperation(MappedStatement mappedStatement, Object parameterObject, RowBounds pageBounds, AbstractDialect dialect){
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.pageBounds = pageBounds;
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.dialect = dialect;
        init();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
	protected void init(){
        boundSql = mappedStatement.getBoundSql(parameterObject);
        parameterMappings = new ArrayList(boundSql.getParameterMappings());
        //判断参数类型
        if(parameterObject != null){
	        if(parameterObject instanceof Map){
	        	//参数类型为map
	            pageParameters.putAll((Map)parameterObject);
	            
	        //如果参数类型不是map
	        }else{
	            Class cls = parameterObject.getClass();
	            //判断是否是基本类型或者数组
	            if(cls.isPrimitive() || cls.isArray() ||
	                    SimpleTypeRegistry.isSimpleType(cls) ||
	                    Enum.class.isAssignableFrom(cls) ||
	                    Collection.class.isAssignableFrom(cls)){
	                for (ParameterMapping parameterMapping : parameterMappings) {
	                    pageParameters.put(parameterMapping.getProperty(),parameterObject);
	                }
	            //如果不是基本类型或者数组
	            }else{
	                MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(parameterObject);
	                ObjectWrapper wrapper = metaObject.getObjectWrapper();
	                for (ParameterMapping parameterMapping : parameterMappings) {
	                    PropertyTokenizer prop = new PropertyTokenizer(parameterMapping.getProperty());
	                    pageParameters.put(parameterMapping.getProperty(),wrapper.get(prop));
	                }
	            }
	        }

        }
        StringBuffer bufferSql = new StringBuffer(boundSql.getSql().trim());
        if(bufferSql.lastIndexOf(";") == bufferSql.length()-1){
            bufferSql.deleteCharAt(bufferSql.length()-1);
        }
        String sql = bufferSql.toString();
        if(pageBounds.getOffset() != RowBounds.NO_ROW_OFFSET
                || pageBounds.getLimit() != RowBounds.NO_ROW_LIMIT){
           // pageSQL = dialect.getLimitString(sql,pageBounds.getOffset(),pageBounds.getLimit());
            setPageParameter("__offset", pageBounds.getOffset(),Integer.class);
            setPageParameter("__limit",pageBounds.getLimit(),Integer.class);
        }
      
    }

    public List<ParameterMapping> getParameterMappings(){
        return parameterMappings;
    }

    public Object getParameterObject(){
        return pageParameters;
    }


    public String getPageSQL(){
        return pageSQL;
    }

    @SuppressWarnings("rawtypes")
	protected void setPageParameter(String name, Object value, Class type){
        ParameterMapping parameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), name, type).build();
        parameterMappings.add(parameterMapping);
        pageParameters.put(name, value);
    }
}
