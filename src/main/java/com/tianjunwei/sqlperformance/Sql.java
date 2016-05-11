package com.tianjunwei.sqlperformance;
/**
 * sql映射id类实体，用于记录sqlId，执行次数total，平均执行时间averageTime,最大执行时间maxTime,带完整参数的sql语句sql
 */
public class Sql {
	
	private String sqlId;
	
	private String sql;
	
	private int total;
	
	private long totalTime;
	
	private long maxTime;
	
	public String getSqlId() {
		return sqlId;
	}
	
	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}
	
	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public long getTotalTime() {
		return totalTime;
	}
	
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	
	public long getMaxTime() {
		return maxTime;
	}
	
	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
	
}
