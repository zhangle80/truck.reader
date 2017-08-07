package org.xz.img;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xz.utils.ConstUtils;

import com.huizhi.data.persistence.MysqlHelper;
import com.huizhi.sdk.IndexStatistic;
import com.huizhi.sdk.SdkQuery;

/**
 * @author leo
 *从FTP中读取XML文件
 */
public class XmlReader {
	private static Logger logger = LoggerFactory.getLogger(XmlReader.class);

	public Map<String,String> reader(InputStream in,String filePath,String fileName) throws DocumentException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		logger.debug("接收到XML字符串流，开始获取XML中图片信息!");
		SAXReader reader = new SAXReader();
	    Document document = reader.read(in);
	   
	    //String localImgDir=this.mkImgLocalPath(filePath);			//旧的文件夹生成方式，按照日期格式生成    	    
        Element root = document.getRootElement();					// 获取根元素
        System.out.println("Root: " + root.getText());
       
        Element data=root.element("Data");							// 获取名字为指定名称的第一个子元素    
        Map<String,String> map=new HashMap<String,String>();
        
        String carNo=data.element("CarNo").getText();
        String localImgDir=this.mkImgLocalPathByType(carNo);		//按照车型，车牌的方式生成,会导致filePath参数不起作用
        
        if("".equals(localImgDir)){
        	return map;
        }
        
        @SuppressWarnings("unchecked")
		Iterator<Element> iterator=data.elementIterator();			//遍历所有属性
        String conPath="";											//图片相对路径
        
        while(iterator.hasNext()){
        	Element element=iterator.next();
        	String name=element.getName();
        	String value=element.getText();
        	String save_img_to_local=ConstUtils.prop.get(ConstUtils.SAVE_IMG_TO_LOCAL).toString();
        	if(name.equals("Picture")){
        		if(save_img_to_local.equals("1")){
                    Element picture=element.element("Pic1");
                    String imgString=picture.getStringValue();
                    
                    ImgReader imgReader=new ImgReader();		//将文件转成图片，并保存
                    String imgLocalPath=imgReader.reader(imgString,fileName,localImgDir);
                    String[] dirs=imgLocalPath.split("\\\\");
                    
                    for(int i=5;i<dirs.length;i++){
                    	if(i<dirs.length-1){
                    		conPath+=dirs[i]+"/";				//获取相对位置,此处有问题,因为配置发生变化获取组的位置也会发生变化,第一个值可能不是从5开始
                    	}else{
                    		conPath+=dirs[i];
                    	}                   	
                    }                   
                    map.put("ImgLocalPath", imgLocalPath.replace("\\", "\\\\"));	
        		}
        		continue;
        	}
        	map.put(name, value);
        }
        
        map.put("FilePath", filePath.replace("\\", "\\\\"));
        map.put("FileName", fileName);
        map.put("CreateDateTime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        map.put("CreateUserName", "leo");
        
        boolean isIllegal=SdkQuery.queryByHttp(conPath);	//向SDK发送识别请求
        IndexStatistic.statistic(isIllegal, carNo);			//开始统计
        IndexStatistic.print();								//打印统计结果
        
        in.close();
        return map;
	}
	
	/**
	 * 按照日期格式生成文件夹结构
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("unused")
	private String mkImgLocalPath(String filePath){
	    String xmlStorePath=ConstUtils.prop.getProperty(ConstUtils.XML_STORE_PATH);
	    String imgStorePath=ConstUtils.prop.getProperty(ConstUtils.IMG_SDK_READ_PATH);
	    
	    String localStorePath=filePath.replace(xmlStorePath, imgStorePath);
	    
	    File localDir=new File(localStorePath);
	    if(!localDir.exists()){
	    	localDir.mkdirs();
	    }
	    
	    return localStorePath;
	}
	
	/**
	 * 输入；车牌号码
	 * 根据车型来生产文件夹结构
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private String mkImgLocalPathByType(String carNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException{
		logger.debug("开始生成车牌["+carNo+"]的文件夹结构");
		String imgStorePath=ConstUtils.prop.getProperty(ConstUtils.IMG_SDK_READ_PATH);
		String type=MysqlHelper.truckTypeQuery(carNo);
		String localStorePath="";
		
		if("".equals(type)){
			logger.info("未查到车牌号["+carNo+"]的类型，该车可能为外地牌照或公交车等非货车黄牌车");
			localStorePath= imgStorePath+File.separator+"error";
		}else{
			localStorePath=imgStorePath+File.separator+type+File.separator+carNo.substring(1);
		}
				
	    File localDir=new File(localStorePath);
	    if(!localDir.exists()){
	    	localDir.mkdirs();
	    }
	    
		return localStorePath;
	}	

}
