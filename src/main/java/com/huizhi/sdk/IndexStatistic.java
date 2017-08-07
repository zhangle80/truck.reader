package com.huizhi.sdk;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huizhi.data.persistence.MysqlHelper;

/**
 * 指标统计功能
 * @author pc
 *
 */
public class IndexStatistic {
	private static Logger logger = LoggerFactory.getLogger(IndexStatistic.class);
	private static int PIC_COUNT=0;				//图片总数
	private static int PASS_COUNT=0;			//车型异常统计总数，该车为外地车，或其他非货车大型车（例如公交车等）
	private static int JING_COUNT=0;			//京牌车总数
	private static int FOREIGN_COUNT=0;			//外地牌照车总数
	private static int TYPE_NORMAL_COUNT=0;		//可以查到车型的货车总数
	private static int QUILTY_NORMAL_COUNT=0;	//可以查到质量的货车总数
	private static int JING_NORMAL_COUNT=0;		//参与统计的货车总数，也即可以查到车型和核载量的货车总数
	private static int TYPE_NULL_COUNT=0;		//车型为空总数
	private static int QUILTY_NULL_COUNT=0;		//核载量为空总数
	private static int RECO_TT_COUNT=0;			//交管查出为8吨以上，且识别为8吨以上的货车总数
	private static int RECO_TF_COUNT=0;			//交管查出为8吨以上，且识别为8吨以下的货车总数
	private static int RECO_FF_COUNT=0;			//交管查出为8吨以下，且识别为8吨以下的货车总数
	private static int RECO_FT_COUNT=0;			//交管查出为8吨以下，且识别为8吨以上的货车总数
	private static int REAL_ILLEGAL_COUNT=0;	//交管查出为8吨以上的货车总数
	private static int RECO_ILLEGAL_COUNT=0;	//识别为8吨以上的货车总数
	private static int QUILTY_NULL_RECO_T=0;	//交管无核载量数据，但识别为违法货车总数
	private static int QUILTY_NULL_RECO_F=0;	//交管无核载量数据，但识别为非违法货车总数
	/**
	 * 图片总数加1
	 * @return
	 */
	private static int increasePicCount(){
		IndexStatistic.PIC_COUNT+=1;
		return IndexStatistic.PIC_COUNT;
	}
	
	/**
	 * 车型异常统计总数，该车为外地车，或其他非货车大型车（例如公交车等），查不到车型和核载量
	 * @return
	 */
	private static int increasePassCount(){
		IndexStatistic.PASS_COUNT+=1;
		return IndexStatistic.PASS_COUNT;
	}
	
	/**
	 * 京牌车总数统计
	 * @return
	 */
	private static int increaseJingCount(){
		IndexStatistic.JING_COUNT+=1;
		return IndexStatistic.JING_COUNT;
	}
	
	/**
	 * 外地牌照车统计
	 * @return
	 */
	private static int increaseForeignCount(){
		IndexStatistic.FOREIGN_COUNT+=1;
		return IndexStatistic.FOREIGN_COUNT;
	}
	
	/**
	 * 可以查到车型的货车总数统计
	 * @return
	 */
	private static int increaseTypeNormalCount(){
		IndexStatistic.TYPE_NORMAL_COUNT+=1;
		return IndexStatistic.TYPE_NORMAL_COUNT;
	}
		
	private static int increaseQuiltyNormalCount(){
		IndexStatistic.QUILTY_NORMAL_COUNT+=1;
		return IndexStatistic.QUILTY_NORMAL_COUNT;
	}
	/**
	 * 参与统计的货车总数，也即可以查到车型和核载量的货车总数
	 * @return
	 */
	private static int increaseJingNormalCount(){
		IndexStatistic.JING_NORMAL_COUNT+=1;
		return IndexStatistic.JING_NORMAL_COUNT;
	}
	
	/**
	 * 车型为空总数
	 * @return
	 */
	private static int increaseTypeNullCount(){
		IndexStatistic.TYPE_NULL_COUNT+=1;
		return IndexStatistic.TYPE_NULL_COUNT;
	}
	
	/**
	 * 核载量为空总数
	 * @return
	 */
	private static int increaseQuiltyNullCount(){
		IndexStatistic.QUILTY_NULL_COUNT+=1;
		return IndexStatistic.QUILTY_NULL_COUNT;
	}
	
	/**
	 * 交管查出为8吨以上，且识别为8吨以上的货车总数
	 * @return
	 */
	private static int increaseRecoTTCount(){
		IndexStatistic.RECO_TT_COUNT+=1;
		return IndexStatistic.RECO_TT_COUNT;
	}
	
	/**
	 * 交管查出为8吨以上，且识别为8吨以下的货车总数
	 * @return
	 */
	private static int increaseRecoTFCount(){
		IndexStatistic.RECO_TF_COUNT+=1;
		return IndexStatistic.RECO_TF_COUNT;
	}
	
	/**
	 * 交管查出为8吨以下，且识别为8吨以下的货车总数
	 * @return
	 */
	private static int increaseRecoFFCount(){
		IndexStatistic.RECO_FF_COUNT+=1;
		return IndexStatistic.RECO_FF_COUNT;
	}
	
	/**
	 * 交管查出为8吨以下，且识别为8吨以上的货车总数
	 * @return
	 */
	private static int increaseRecoFTCount(){
		IndexStatistic.RECO_FT_COUNT+=1;
		return IndexStatistic.RECO_FT_COUNT;
	}
	
	/**
	 * 交管查出8吨以上货车总数
	 * @return
	 */
	private static int increaseRealIllegalCount(){
		IndexStatistic.REAL_ILLEGAL_COUNT+=1;
		return IndexStatistic.REAL_ILLEGAL_COUNT;
	}
	
	/**
	 * SDK识别出货车总数
	 * @return
	 */
	private static int increaseRecoIllegalCount(){
		IndexStatistic.RECO_ILLEGAL_COUNT+=1;
		return IndexStatistic.RECO_ILLEGAL_COUNT;
	}
	
	private static int increaseQuiltyNullRecoTCount(){
		IndexStatistic.QUILTY_NULL_RECO_T+=1;
		return IndexStatistic.QUILTY_NULL_RECO_T;
	}
	
	private static int increaseQuiltyNullRecoFCount(){
		IndexStatistic.QUILTY_NULL_RECO_F+=1;
		return IndexStatistic.QUILTY_NULL_RECO_F;
	}
	
	/**
	 * @param isIllegal
	 * @param carNo
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void statistic(boolean isIllegal,String carNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException{
		logger.info("SDK结果已经返回开始统计指标,该车车牌号为:["+carNo+"]");
		increasePicCount();								//总数加1
		
		String type=MysqlHelper.truckTypeQuery(carNo);	//获取车型
		int quilty=MysqlHelper.truckQulityQuery(carNo);	//获取核载量
		
		logger.info("货车：["+carNo+"]，该货车车型为["+type+"],核载量为：["+quilty+"],识别结果为："+isIllegal);
		
		if("".equals(type)){
			logger.info("["+carNo+"]，该货车查不到车型");
			increaseTypeNullCount();					//类型为空加1
			increasePassCount();						//车型异常加1
		}else{
			increaseTypeNormalCount();					//有类型值加1
		}
		
		if(quilty==0){
			logger.info("["+carNo+"],该货车查不到核载量");
			increaseQuiltyNullCount();					//质量为空加1
			increasePassCount();						//车型异常加1
			
			if(isIllegal){
				increaseQuiltyNullRecoTCount();			//车管无数据，但统计为违法总数加1
			}else{
				increaseQuiltyNullRecoFCount();			//车管无数据，但统计为合法总数加1
			}
		}else{
			increaseQuiltyNormalCount();				//有质量值加1
		}
		
		if(!"".equals(type)&&quilty!=0){
			increaseJingNormalCount();
		}
		
		if(carNo.contains("京")){
			logger.info("["+carNo+"]，该货车为京牌车");
			increaseJingCount();						//京牌车加1
		}else{
			logger.info("["+carNo+"]，该货车为外地车");
			increaseForeignCount();						//外地车加1
		}
		
		if(quilty>=8000){								//交管查出为8吨及以上
			increaseRealIllegalCount();
			if(isIllegal){
				increaseRecoIllegalCount();
				logger.info("类型为:["+type+"]，车牌为:["+carNo+"]，该货车核载质量为"+quilty+"且识别为"+"违法");
				increaseRecoTTCount();
			}else{
				logger.warn("类型为:["+type+"]，车牌为:["+carNo+"]，该货车核载质量为"+quilty+"但识别为"+"合法");
				increaseRecoTFCount();
			}
		}
		
		if(quilty>0&&quilty<8000){
			if(isIllegal){
				increaseRecoIllegalCount();
				logger.warn("类型为:["+type+"]，车牌为:["+carNo+"]，该货车核载质量为"+quilty+"且识别为"+"违法");
				increaseRecoFTCount();
			}else{
				logger.info("类型为:["+type+"]，车牌为:["+carNo+"]，该货车核载质量为"+quilty+"但识别为"+"合法");
				increaseRecoFFCount();
			}
		}
	}
	
	public static void print(){
		logger.info("指标统计结束,当前指标结果为：");
		logger.info("图片总数："+PIC_COUNT);
		logger.info("异常车型总数："+PASS_COUNT);
		logger.info("京牌总数："+JING_COUNT);
		logger.info("外地车总数："+FOREIGN_COUNT);
		logger.info("车型获取正常总数："+TYPE_NORMAL_COUNT);
		logger.info("核载量获取正常总数："+QUILTY_NORMAL_COUNT);
		logger.info("参与统计的货车总数："+JING_NORMAL_COUNT);
		logger.info("类型为空总数："+TYPE_NULL_COUNT);
		logger.info("核载量为空总数："+QUILTY_NULL_COUNT);
		logger.info("8吨以上，且识别为违法货车总数："+RECO_TT_COUNT);
		logger.info("8吨以上，但识别为合法货车总数："+RECO_TF_COUNT);
		logger.info("8吨以下，且识别为合法货车总数："+RECO_FF_COUNT);
		logger.info("8吨以下，但识别为违法货车总数："+RECO_FT_COUNT);
		logger.info("交管判别为8吨以上货车总数："+REAL_ILLEGAL_COUNT);
		logger.info("SDK识别为违法货车总数："+RECO_ILLEGAL_COUNT);
		logger.info("交管无核载量数据，但识别为违法货车总数："+QUILTY_NULL_RECO_T);
		logger.info("交管无核载量数据，但识别为合法货车总数："+QUILTY_NULL_RECO_F);
	}
}
