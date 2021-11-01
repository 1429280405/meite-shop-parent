package com.mayikt.zuul.handler.impl;

import com.mayikt.zuul.handler.BaseHandler;
import com.mayikt.zuul.handler.GatewayHandler;
import com.mayikt.zuul.mapper.BlacklistMapper;
import com.mayikt.zuul.mapper.entity.MeiteBlacklist;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujinqiang
 * @create 2021-03-03 21:03
 */
@Slf4j
@Component
public class BlackListHandler extends BaseHandler implements GatewayHandler {
    @Autowired
    private BlacklistMapper blacklistMapper;

    @Override
    public Boolean service(RequestContext ctx, String ipAddres, HttpServletRequest request,
                           HttpServletResponse response) {
        // >>>>>>>>>>>>>黑名单拦截操作<<<<<<<<<<<<<<<<<<<
        log.info(">>>>>>>>>拦截1 黑名单拦截 ipAddres:{}<<<<<<<<<<<<<<<<<<<<<<<<<<", ipAddres);
        MeiteBlacklist meiteBlacklist = blacklistMapper.findBlacklist(ipAddres);
        if (meiteBlacklist != null) {
            resultError(ctx, "ip:" + ipAddres + ",Insufficient access rights");
            return Boolean.FALSE;
        }
        // 传递给下一个
        gatewayHandler.service(ctx, ipAddres, request, response);
        return Boolean.TRUE;
    }

    @Override
    public void setNextHandler(GatewayHandler gatewayHandler) {

    }

    @Override
    public GatewayHandler getNextHandler() {
        return null;
    }
}
