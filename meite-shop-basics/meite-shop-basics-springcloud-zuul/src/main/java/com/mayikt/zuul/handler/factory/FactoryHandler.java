package com.mayikt.zuul.handler.factory;

import com.mayikt.core.utils.SpringContextUtil;
import com.mayikt.zuul.handler.GatewayHandler;

/**
 * @author liujinqiang
 * @create 2021-03-03 21:20
 */
public class FactoryHandler {

    public static GatewayHandler getHandler(){
        // 1.黑名单拦截
        GatewayHandler handler1 = (GatewayHandler) SpringContextUtil.getBean("blackListHandler");
        // 2.验证accessToken
        GatewayHandler handler2 = (GatewayHandler) SpringContextUtil.getBean("apiAuthorityHandler");
        handler1.setNextHandler(handler2);
        // 3.API接口参数接口验签
        GatewayHandler handler3 = (GatewayHandler) SpringContextUtil.getBean("toVerifyMapHandler");
        handler2.setNextHandler(handler3);
        return handler1;    }
}
