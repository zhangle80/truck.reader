package org.xz.boot;

import java.io.IOException;
import java.sql.SQLException;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xz.task.FileScan;
import org.xz.utils.ConstUtils;

/**
 * @author leo
 *引导程序，程序入口
 */
public class Boot {
	private static Logger logger = LoggerFactory.getLogger(Boot.class);
	private static String version = "1.0.0";//当前系统版本
	
	/**
	 * @param args
	 * 程序主线程
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws DocumentException, IOException, ClassNotFoundException, SQLException {
        // 记录debug级别的信息
        logger.info("您好，读取XML文件系统启动，当前系统的版本号是"+Boot.version);
        // 读取配置参数，并将参数存储在内存中
        ConstUtils.scanConst();
        // 启动刷新任务
        FileScan.doScanPathFile();
	}

}
