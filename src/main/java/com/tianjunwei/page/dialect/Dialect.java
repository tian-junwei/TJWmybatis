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
* @Description: ������  
* @author tianjunwei  tiantianjunwei@126.com   
* @date 2016��3��31�� ����11:10:04  
* @version V1.0    
*/
public class Dialect {
    protected TypeHandlerRegistry typeHandlerRegistry;
    protected MappedStatement mappedStatement;
    protected RowBounds pageBounds;
    protected Object parameterObject;
    protected BoundSql boundSql;
    protected List<ParameterMapping> parameterMappings;
    protected Map<String, Object> pageParameters = new HashMap<String, Object>();
    private String pageSQL;

    
    public Dialect(MappedStatement mappedStatement, Object parameterObject, RowBounds pageBounds){
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.pageBounds = pageBounds;
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        init();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
	protected void init(){
        boundSql = mappedStatement.getBoundSql(parameterObject);
        parameterMappings = new ArrayList(boundSql.getParameterMappings());
        //�жϲ��������Ƿ���һ��map
        if(parameterObject != null){
	        if(parameterObject instanceof Map){
	        	//�����map��ֱ��׷�ӵ�pageParameters
	            pageParameters.putAll((Map)parameterObject);
	            
	        //�������map�Ҳ�Ϊ�����ղ���put��pageParameters��
	        }else{
	            Class cls = parameterObject.getClass();
	            //�ж����Ƿ���һ���������ͣ����飬�Ƿ��ǻ������͵Ķ��󣬼��Ƿ��Ǽ���
	            if(cls.isPrimitive() || cls.isArray() ||
	                    SimpleTypeRegistry.isSimpleType(cls) ||
	                    Enum.class.isAssignableFrom(cls) ||
	                    Collection.class.isAssignableFrom(cls)){
	                for (ParameterMapping parameterMapping : parameterMappings) {
	                    pageParameters.put(parameterMapping.getProperty(),parameterObject);
	                }
	            //��������������ͣ�����ɶ���
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
            pageSQL = getLimitString(sql, "__offset", pageBounds.getOffset(), "__limit",pageBounds.getLimit());
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

    /**
     * @Title: getLimitString 
     * @Description: ��sql��ɷ�ҳsql���
     * @param sql
     * @param offsetName
     * @param offset
     * @param limitName
     * @param limit
     * @return    
     * String     
     * @throws
     */
    protected String getLimitString(String sql, String offsetName,int offset, String limitName, int limit) {
        throw new UnsupportedOperationException("paged queries not supported");
    }
}