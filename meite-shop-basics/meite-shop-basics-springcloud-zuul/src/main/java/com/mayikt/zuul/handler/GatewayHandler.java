package com.mayikt.zuul.handler;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujinqiang
 * @create 2021-03-03 20:59
 */
public interface GatewayHandler {

    /**
     * 网关拦截执行方法
     * @param ctx
     * @param ipAddres
     * @param request
     * @param response
     * @return
     */
    Boolean service(RequestContext ctx, String ipAddres, HttpServletRequest request, HttpServletResponse response);

    /**
     * 设置下一个handler
     * @param gatewayHandler
     */
    void setNextHandler(GatewayHandler gatewayHandler);


    /**
     * 获取下一个handler
     * @return
     */
    public GatewayHandler getNextHandler();

}
