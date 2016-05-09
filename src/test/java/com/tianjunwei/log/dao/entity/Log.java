/** 
*@ProjectName: TTRabbitMQ 
*@FileName: Log.java 
*@Date: 2016年4月15日 
*@Copyright: 2016 tianjunwei All rights reserved. 
*/  
package com.tianjunwei.log.dao.entity;

/**    
* @Title: Log.java  
* @Package com.tianjunwei.log.dao.entity  
* @Description: 日志实体类  
* @author tianjunwei  tiantianjunwei@126.com   
* @date 2016年4月15日 上午12:03:32  
* @version V1.0    
*/
public class Log {
	private int id;
	private int logType;
	private String logInfo;
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
	 * @return the logType
	 */
	public int getLogType() {
		return logType;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(int logType) {
		this.logType = logType;
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
