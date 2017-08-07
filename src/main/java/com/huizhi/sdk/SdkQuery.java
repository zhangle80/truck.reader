package com.huizhi.sdk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.huizhi.sdk.InfoLoad;;;

public class SdkQuery {
	
	private static Logger logger = LoggerFactory.getLogger(SdkQuery.class);
    /**
     * 向SDK图像识别程序进行查询
     * @param scanPath
     * @param storePath
     * @param xmlName
     * @return
     */
    public static boolean queryByHttp(String xmlName){
    	boolean isIllegal=false;
    	String imgName=xmlName.split("\\.")[0]+".jpg";
    	logger.debug("sdk识别图片："+imgName);
    	
    	InfoLoad.getInfoLoadInstance();
    	String queryString="http://localhost/cgi-bin/truckIllegal/car.cgi?imgName=vion/"+imgName;
		String sdkRecognizationResult=InfoLoad.loadForString(queryString, 0);
    	
    	isIllegal=SdkQuery.isIllegal(sdkRecognizationResult, imgName);

    	return isIllegal;
    }
    
    private static boolean isIllegal(String sdkRecognizationResult,String imgName){
    	boolean result=false;
    	
    	Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
    	Matcher matcher = pattern.matcher(sdkRecognizationResult);
    	
    	while(matcher.find()){
    		String illegal=matcher.group();
    		if(illegal.equals("illegal:1")){
    			result=true;
    			logger.info("识别到违法大货车，图片名字为："+imgName);
    			break;
    		}
    	}
    	return result;
    }
}
