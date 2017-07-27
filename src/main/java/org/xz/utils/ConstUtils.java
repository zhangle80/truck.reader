package org.xz.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leo
 *系统常量加载
 */
public class ConstUtils {
	public static Properties prop = new Properties();
	private static Logger logger = LoggerFactory.getLogger(ConstUtils.class);
	

	public static String IMG_SDK_READ_PATH="img.sdk.read.path";	//和SDK的CGI进行图片识别的图片交互路径，本程序把图片放入该地址，然后通知SDK进行识别
	public static String XML_STORE_PATH="xml.store.path";		//XML的本地存储路径
	public static String HTTPCLIENT_CONNECT_TIMEOUT="httpclient.connect.timeout";
	public static String HTTPCLIENT_SOCKET_TIMEOUT="httpclient.socket.timeout";
	public static String HTTPCLIENT_CONNECTION_COUNT="httpclient.connection.count";
	public static String HTTPCLIENT_MAXPERROUTE_COUNT="httpclient.maxperroute.count";
	public static String IMG_CUT_X="img.cut.x";//截取图片的X坐标值，文安给的图片是拼接的图片，只需要拼接的第一幅图片，所以需要截取
	public static String IMG_CUT_Y="img.cut.y";//截取图片的X坐标值
	public static String IMG_CUT_W="img.cut.w";//截取图片的宽度
	public static String IMG_CUT_H="img.cut.h";//截取图片的高度
	
	public static String DB_URL="db.url";
	public static String DB_DRIVER="db.driver";
	public static String DB_USERNAME="db.username";
	public static String DB_PASSWORD="db.password";
	
	public static String SAVE_IMG_TO_LOCAL="save_img_to_local";
	public static String SAVE_INF_TO_DB="save_inf_to_db";
	
	
	public static void scanConst(){	
		logger.debug("开始读取常量属性文件...");
		try {
			String filePath = ConstUtils.class.getClassLoader().getResource("const.properties").getPath();
			//String filePath = "D://soft//truck//resources//const.properties";
			logger.debug("常量属性文件地址:"+filePath);
			//读取属性文件a.properties
			InputStream in = new BufferedInputStream (new FileInputStream(filePath));
			//加载属性列表
			prop.load(in); 
			
			Iterator<String> it=prop.stringPropertyNames().iterator();			
			while(it.hasNext()){
				String key=it.next();
				logger.debug("读取到属性信息，"+key+"="+prop.getProperty(key));
			}
			
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
