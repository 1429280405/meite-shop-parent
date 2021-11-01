package com.mayikt.zuul.build;

import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujinqiang
 * @create 2021-02-27 14:04
 */
public interface GatewayBuild {
    /**
     * 黑名单拦截
     */
    Boolean blackBlock(RequestContext ctx, String ipAddres, HttpServletResponse response);

    /**
     * 参数验证
     */
    Boolean toVerifyMap(RequestContext ctx, String ipAddres, HttpServletRequest request);

    /**
     * 接口权限验证
     */
    public Boolean apiAuthority(RequestContext ctx, HttpServletRequest request) ;
}
