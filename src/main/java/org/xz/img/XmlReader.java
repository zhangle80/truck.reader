package org.xz.img;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leo
 *从FTP中读取XML文件
 */
public class XmlReader {
	private static Logger logger = LoggerFactory.getLogger(XmlReader.class);

	public Map<String,String> reader(InputStream in) throws DocumentException, IOException{
		logger.debug("接收到XML字符串流，开始获取XML中图片的Base64位字符串!");
		SAXReader reader = new SAXReader();
	    Document document = reader.read(in);
	    
	    // 获取根元素
        Element root = document.getRootElement();
        System.out.println("Root: " + root.getName());

        // 获取名字为指定名称的第一个子元素     
        Element data=root.element("Data");
        Map<String,String> map=new HashMap<String,String>();
        
        @SuppressWarnings("unchecked")
		Iterator<Element> iterator=data.elementIterator();
        while(iterator.hasNext()){
        	Element element=iterator.next();
        	String name=element.getName();
        	String value=element.getText();
        	if(name.equals("Picture")){
        		continue;
        	}
        	map.put(name, value);
        }
        
        in.close();
        return map;
	}
}
