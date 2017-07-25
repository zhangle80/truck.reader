package org.xz.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xz.img.XmlReader;
import org.xz.utils.ConstUtils;

import com.huizhi.data.persistence.MysqlHelper;

public class FileScan {
	private static Logger logger = LoggerFactory.getLogger(FileScan.class);
	private static List<Map<String,String>> trucks=new ArrayList<Map<String,String>>();
	/**
	 * 扫描文件保存路径
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void doScanPathFile() throws DocumentException, IOException, ClassNotFoundException, SQLException{
		String scanPath=ConstUtils.prop.getProperty(ConstUtils.XML_STORE_PATH); 	//扫描的FTP文件路径
		logger.debug("XML File Root Path==>" + scanPath); 
		
		File root=new File(scanPath);
		FileScan.getDirectory(root);	
		
		MysqlHelper.writerToTruck(trucks);
	}
	
	/**
	 * 递归遍历
	 * @param file 根目录
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private static void getDirectory(File file) throws DocumentException, IOException, ClassNotFoundException, SQLException {
		
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
			
		for (File f : flist) {
			String filePath=f.getAbsolutePath();
			if (f.isDirectory()) {
				//这里将列出所有的文件夹
				logger.debug("Dir==>" + filePath); 
				getDirectory(f);
			} else {
				//这里将列出所有的文件
				logger.debug("File==>" + filePath);
				
				String fileName=f.getName();
				if(!FileScan.validate(fileName)){
					continue;
				}else{
	                XmlReader xmlReader=new XmlReader();				//从文件中提取文件字符串
	                InputStream fis=new FileInputStream(filePath);		
	                
	                Map<String,String> map=xmlReader.reader(fis);
	                map.put("filePath", filePath);
	                map.put("fileName", fileName);
	                
	                trucks.add(map);
				}
			}
		}		
	}
	
	/**
	 * 验证文件是否是XML文件
	 * @param xmlName
	 * @return
	 */
	private static boolean validate(String xmlName){
    	
        if(!xmlName.endsWith(".xml")){						//验证是否XML文件
        	logger.debug("该文件:"+xmlName+"不是xml文件，跳过!");
        	return false;
        }
        logger.debug("发现新文件:"+xmlName+"!"); 
        return true;
	}

}
