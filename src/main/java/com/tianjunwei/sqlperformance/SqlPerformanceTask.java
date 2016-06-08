package com.tianjunwei.sqlperformance;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *将sql语句执行性能记录到csv文件中 ，利用spring的定时任务来执行此类的run方法
 */
public class SqlPerformanceTask{

	 private static Logger log = LoggerFactory.getLogger(SqlPerformanceTask.class);
	 private String folder;
	 
	public void run() {
		long start = System.currentTimeMillis();
        if (log.isDebugEnabled())
            log.debug("SqlPerformanceTask begin...");
        saveAsCSVFile();
        if (log.isDebugEnabled())
            log.debug("SqlPerformanceTask end...");
        long lasttimeDuration = System.currentTimeMillis() - start;
        if (log.isDebugEnabled())
        	log.debug("SqlPerformanceTask total time: "+ lasttimeDuration);		
	}
	/** 
	 *  将获得的sql语句记录在csv文件中 
	 */ 
	private void saveAsCSVFile() {
		 	String date=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	    	String path = System.getProperty("user.dir");
	        String filePath = path + "/" + "performance" + "/" + date;
	        if (folder != null && folder !="") {
	        	filePath = filePath + "/" + folder;
	        }
			File filedir = new File(filePath);  //文件目录
			if(!filedir.exists()){
				filedir.mkdirs();
			}
			if(log.isDebugEnabled()){
				log.debug("the floder path is:"+filePath+"/"+date+".csv");
			}
			File file = new File(filePath+"/"+date+".csv");  //文件
			//File file = new File("recoder.csv");  //文件
	        FileOutputStream out = null;
	        OutputStreamWriter osw = null;
	        BufferedWriter bw = null;
			try {
				out = new FileOutputStream(file);
		        osw = new OutputStreamWriter(out, "GBK");
		        bw = new BufferedWriter(osw);
		        bw.write(getData());
		        bw.flush();
		        osw.flush();
		        out.flush(); 
			} catch (Exception e) {
				log.error("io exception"+e);
			} finally {
				try {
					if(bw != null){
						bw.close();
					}
					if(osw != null){
						osw.close();
					}
			        if(out != null){
			        	out.close(); 
			         }
				} catch (IOException e) {
					log.error("io close exception"+e);
				}
			}
	}
	/**
	 *  从SqlPerformanceInterceptor类中获得sql语句
	 */
	public String getData(){
		StringBuilder column = new StringBuilder();
		Map<String, SqlEntity> sqlMap= SqlPerformanceInterceptor.sqlMap;
		column.append("sqlId,sql,avg_time,max_time,frequency\r\n");
		for (Map.Entry<String, SqlEntity> entry : sqlMap.entrySet()) {
			SqlEntity sql = entry.getValue();
			column.append(sql.getSqlId());
			column.append(",");
			column.append("\""+sql.getSql()+"\"");
			column.append(",");
			column.append((sql.getTotalTime()/sql.getTotal()));
			column.append(",");
			column.append(sql.getMaxTime());
			column.append(",");
			column.append(sql.getTotal());
			column.append("\r\n");
		}
		return column.toString();
	}
	
	public void setFolder(String folder) {
		this.folder = folder;
	}

}
