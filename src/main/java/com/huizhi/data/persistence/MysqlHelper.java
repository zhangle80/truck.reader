package com.huizhi.data.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huizhi.utils.db.DBUtil;


public class MysqlHelper {
	private static Logger logger = LoggerFactory.getLogger(MysqlHelper.class);
	private static Connection con = null;
	private static Map<String,Map<String,Object>> qulityMap=null;//车牌号查询核定载质量的哈希表
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
				sql.append("INSERT INTO truck_yuxin ");
				
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


	/**
	 * 根据车牌查询核定载质量、车型等内容
	 * @param carNo 车牌号
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Map<String,Map<String,Object>> truckQuery() throws ClassNotFoundException, SQLException, IOException, InstantiationException, IllegalAccessException{
		if(MysqlHelper.qulityMap==null){
			MysqlHelper.qulityMap=new HashMap<String,Map<String,Object>>();
			
			con = DBUtil.openConnection();
			String sql="select QHP,CLXH,CLLX,HEDINGZHILIANG from truck";
			
			List<Map<String,Object>> list= DBUtil.queryMapList(con, sql);
			
			if(list!=null&&list.size()>0){
				for(Map<String,Object> truck:list){
					String QHP=truck.get("QHP").toString();
					MysqlHelper.qulityMap.put(QHP, truck);
				}
			}
		}
		return MysqlHelper.qulityMap;
	}
	
	/**
	 * 查询核定载质量
	 * @param carNo
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static int truckQulityQuery(String carNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException{
		Map<String,Map<String,Object>> truckListMap=truckQuery();
		if(truckListMap!=null&&truckListMap.size()>0){
			Map<String,Object> truckMap=truckListMap.get(carNo);
			if(truckMap!=null&&truckMap.size()>0){
				if((truckMap.get("HEDINGZHILIANG")!=null)&&(!truckMap.get("HEDINGZHILIANG").equals(""))){
					int qulity=Integer.parseInt(truckMap.get("HEDINGZHILIANG").toString());
					return qulity;
				}
			}			
		}
		return 0;
	}
	
	/**
	 * 查询车辆类型编码
	 * @param carNo
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String truckTypeQuery(String carNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException{
		Map<String,Map<String,Object>> truckListMap=truckQuery();
		if(truckListMap!=null&&truckListMap.size()>0){
			Map<String,Object> truckMap=truckListMap.get(carNo);
			if(truckMap!=null&&truckMap.size()>0){
				if((truckMap.get("CLXH")!=null)&&(!truckMap.get("CLXH").toString().equals(""))){
					String type=truckMap.get("CLXH").toString();
					return type;
				}
			}
		}
		return "";
	}
}
