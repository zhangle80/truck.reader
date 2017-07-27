package org.xz.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
		FileScan.processXML(root);	
		String save_inf_to_db=ConstUtils.prop.get(ConstUtils.SAVE_INF_TO_DB).toString();
		if(save_inf_to_db.equals("1")){
			MysqlHelper.writerToDB(trucks);
		}
		
	}
	
	/**
	 * 递归遍历保存XML的文件，将XML的属性信息保存到内存中，同时将图片保存到图片文件夹
	 * @param file 根目录
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private static void processXML(File file)  {
		
		File flist[] = file.listFiles();
		if (flist == null || flist.length == 0) {
			return;
		}
			
		for (File f : flist) {
			String filePath=f.getAbsolutePath();
			if (f.isDirectory()) {
				//这里将列出所有的文件夹
				logger.debug("Dir==>" + filePath); 
				processXML(f);
			} else {
				//这里将列出所有的文件
				logger.debug("File==>" + filePath);
				
				String fileName=f.getName();
				if(!FileScan.validate(fileName)){
					continue;
				}else{
					try {
		                XmlReader xmlReader=new XmlReader();								//从文件中提取文件字符串
		                InputStream fis = new FileInputStream(filePath);
						
		                Map<String,String> map=xmlReader.reader(fis,f.getParent(),fileName);//读取XML文件信息，并将图片保存到本地
		                String save_inf_to_db=ConstUtils.prop.get(ConstUtils.SAVE_INF_TO_DB).toString();
		                if(save_inf_to_db.equals("1")){
		                	trucks.add(map);													//将属性信息放入列表中，用于入库
		                }
		                
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (DocumentException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}		
	                
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
