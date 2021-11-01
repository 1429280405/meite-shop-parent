package com.mayikt.pay.callback.union;//package com.mayikt.pay.callback.union;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mayikt.base.BaseResponse;
//
///**
// * 同步回调与异步回调信息封装
// * 
// * 
// * @description:
// * @author: 97后互联网架构师-余胜军
// * @contact: QQ644064779、微信yushengjun644 www.mayikt.com
// * @date: 2019年1月3日 下午3:03:17
// * @version V1.0
// * @Copyright 该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
// *            私自分享视频和源码属于违法行为。
// */
//public abstract class AbstractPayCallback {
//
//	/**
//	 * 同步回调
//	 * 
//	 * @return
//	 */
//	abstract BaseResponse<JSONObject> synch(HttpServletRequest req, HttpServletResponse resp);
//
//	/**
//	 * 异步回调
//	 * 
//	 * @return
//	 */
//	abstract String async(HttpServletRequest req, HttpServletResponse resp);
//
//	public void asyncCallBack(HttpServletRequest req, HttpServletResponse resp) {
//		// 1.获取第三方支付回调参数信息
//		analyzingPaymentParameter();
//		// 2.将支付参数信息根据支付id插入到数据库中
//		PayLog();
//		// 3.处理异步回调相关日志信息
//		async(req, resp);
//	}
//
//	/**
//	 * 解析日志报文参数 返回对应 支付id和报文参数
//	 * 
//	 * @param req
//	 * @param resp
//	 * @return
//	 */
//	abstract Map<String, String> analyzingPaymentParameter(HttpServletRequest req, HttpServletResponse resp);
//}
