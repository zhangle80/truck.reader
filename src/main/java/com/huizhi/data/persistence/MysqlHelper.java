package com.huizhi.data.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huizhi.utils.db.DBUtil;


public class MysqlHelper {
	private static Logger logger = LoggerFactory.getLogger(MysqlHelper.class);
	private static Connection con = null;
	
	/**
	 * 从XML中读取到的属性信息入库
	 * @param trucks
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static boolean writerToDB(List<Map<String,String>> trucks) throws ClassNotFoundException, SQLException, IOException{
		logger.debug("开始写入数据到Mysql货车库！");
		if(trucks==null||trucks.size()==0){
			return false;
		}else{
			con = DBUtil.openConnection();
			
			int total=trucks.size();
			int current=0;
							
			for(Map<String,String> truck:trucks){
				StringBuilder sql=new StringBuilder();
				current+=1;
				sql.append("INSERT INTO truck ");
				
				StringBuilder keysql=new StringBuilder();
				StringBuilder valuesql=new StringBuilder();
				
				for(String key:truck.keySet()){
					keysql.append(key).append(",");
					String value = truck.get(key).toString();
					
					valuesql.append("'"+value+"'").append(",");
				}
				keysql.deleteCharAt(keysql.length()-1);
				valuesql.deleteCharAt(valuesql.length()-1);
				
				sql.append("(").append(keysql.toString()).append(")").append(" VALUES ").append("(").append(valuesql.toString()).append(")");				
				logger.debug("total="+total+",current="+current+",sql="+sql.toString());
				
				DBUtil.execute(con, sql.toString());//执行写入
			}
			DBUtil.closeConnection();
		}
		return false;
		
	}

}
