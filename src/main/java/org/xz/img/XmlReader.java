package org.xz.img;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

/**
 * @author leo
 *从FTP中读取XML文件
 */
public class XmlReader {
	private static Logger logger = LoggerFactory.getLogger(XmlReader.class);

	public Map<String,String> reader(InputStream in,String filePath,String fileName) throws DocumentException, IOException{
		logger.debug("接收到XML字符串流，开始获取XML中图片的Base64位字符串!");
		SAXReader reader = new SAXReader();
	    Document document = reader.read(in);
	   
	    String localImgDir=this.mkImgLocalPath(filePath);
    	    
        Element root = document.getRootElement();					// 获取根元素
        System.out.println("Root: " + root.getName());
       
        Element data=root.element("Data");							// 获取名字为指定名称的第一个子元素    
        Map<String,String> map=new HashMap<String,String>();
        
        @SuppressWarnings("unchecked")
		Iterator<Element> iterator=data.elementIterator();			//遍历所有属性
        while(iterator.hasNext()){
        	Element element=iterator.next();
        	String name=element.getName();
        	String value=element.getText();
        	
        	if(name.equals("Picture")){
                Element picture=element.element("Pic1");
                String imgString=picture.getStringValue();
                
                ImgReader imgReader=new ImgReader();				//将文件转成图片，并保存
                String imgLocalPath=imgReader.reader(imgString,fileName,localImgDir);
                
                map.put("ImgLocalPath", imgLocalPath);
        		continue;
        	}
        	map.put(name, value);
        }
        
        map.put("FilePath", filePath);
        map.put("FileName", fileName);
        DateTime dateTime=new DateTime();
        map.put("CreateDateTime", dateTime.toString());
        map.put("CreateUserName", "leo");
        
        in.close();
        return map;
	}
	
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
}
