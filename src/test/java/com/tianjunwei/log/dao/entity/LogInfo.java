/** 
*@ProjectName: TTRabbitMQ 
*@FileName: LogInfo.java 
*@Date: 2016年4月18日 
*@Copyright: 2016 tianjunwei All rights reserved. 
*/  
package com.tianjunwei.log.dao.entity;

/**    
* @Title: LogInfo.java  
* @Package com.tianjunwei.log.dao.entity  
* @Description: log日志信息实体 
* @author tianjunwei  tiantianjunwei@126.com   
* @date 2016年4月18日 下午10:32:54  
* @version V1.0    
*/
public class LogInfo {
	private int id;
	private String className;
	private String logInfo;
	private String exception;
	private String createTime;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the logInfo
	 */
	public String getLogInfo() {
		return logInfo;
	}
	/**
	 * @param logInfo the logInfo to set
	 */
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
