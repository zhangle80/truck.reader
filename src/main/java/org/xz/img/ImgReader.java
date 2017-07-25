package org.xz.img;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xz.utils.Base64Utils;
import org.xz.utils.ConstUtils;

public class ImgReader {
	private static Logger logger = LoggerFactory.getLogger(ImgReader.class);
	/**
	 * @param imgString base64二进制图片字节字符串
	 * @return 生成的img文件的路径
	 */
	public String reader(String imgString){
		return this.reader(imgString, "test");
	}
	
	public String reader(String imgString,String imgName){
		logger.debug("接收到图片的Base64位字符串流，开始进行Base64转码，以获取图片源码!");
		String temp=imgName.split("\\.")[0];
		String path=ConstUtils.prop.getProperty(ConstUtils.IMG_SDK_READ_PATH)+File.separator+temp+".jpg";
		
		if(Base64Utils.GenerateImage(imgString, path)){
			return path;
		}else{
			return "";
		}
	}
}
