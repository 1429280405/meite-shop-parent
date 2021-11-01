package com.mayikt.zuul.build;

import com.mayikt.zuul.handler.GatewayHandler;
import com.mayikt.zuul.handler.factory.FactoryHandler;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujinqiang
 * @create 2021-02-27 14:22
 */
@Component
public class GatewayDirector {

    @Autowired
    private GatewayBuild gatewayBuild;

    public void direcot(RequestContext ctx, String ipAddres, HttpServletResponse response, HttpServletRequest request){
       /* *//**
         * 黑名单拦截
         *//*
        Boolean result = gatewayBuild.blackBlock(ctx, ipAddres, response);
        if(!result){
            return;
        }
        *//**
         * 参数验证
         *//*
        Boolean verifyMap = gatewayBuild.toVerifyMap(ctx, ipAddres, request);
        if(!verifyMap){
            return;
        }
        *//**
         * 接口权限验证
         *//*
        Boolean apiAuthority = gatewayBuild.apiAuthority(ctx, request);
        if(!apiAuthority){
            return;
        }*/
        //使用责任链设计方法
        GatewayHandler gatewayHandler = FactoryHandler.getHandler();
        gatewayHandler.service(ctx,ipAddres,request,response);

    }

}
