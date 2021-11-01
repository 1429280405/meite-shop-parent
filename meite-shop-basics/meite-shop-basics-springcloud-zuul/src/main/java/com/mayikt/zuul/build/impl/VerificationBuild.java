package com.mayikt.zuul.build.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.sign.SignUtil;
import com.mayikt.zuul.build.GatewayBuild;
import com.mayikt.zuul.feign.AuthorizationServiceFeign;
import com.mayikt.zuul.mapper.BlacklistMapper;
import com.mayikt.zuul.mapper.entity.MeiteBlacklist;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-02-27 14:09
 */
@Component
@Slf4j
public class VerificationBuild  implements GatewayBuild {

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private AuthorizationServiceFeign authorizationServiceFeign;

    @Override
    public Boolean blackBlock(RequestContext ctx, String ipAddres, HttpServletResponse response) {
        //查询数据库黑名单
        MeiteBlacklist blacklist = blacklistMapper.findBlacklist(ipAddres);
        if(blacklist != null){
            resultError(ctx,"该ip已被拉黑！");
        }
        log.info("ip:"+ipAddres+"已通过验证！");
        response.addHeader("ipAddres",ipAddres);
        return true;
    }

    private void resultError(RequestContext ctx, String errorMsg) {
        ctx.setResponseStatusCode(401);
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(errorMsg);

    }

    @Override
    public Boolean toVerifyMap(RequestContext ctx, String ipAddres, HttpServletRequest request) {
        Map<String, String> verifyMap = SignUtil.toVerifyMap(request.getParameterMap(), false);
        if(!SignUtil.verify(verifyMap)){
            resultError(ctx,"ipAddres:"+ipAddres+"签名验证失败！");
            return false;
        }
        return true;
    }

    @Override
    public Boolean apiAuthority(RequestContext ctx, HttpServletRequest request) {
        //获取接口请求前7位，判断是否包含public，不包含放行
        String requestUrl = request.getServletPath();
        if(!requestUrl.substring(0,7).contains("public")){
            return true;
        }
        //包含则调用AuthorizationServiceFeign接口验证
        //获取请求token
        String accessToken = request.getParameter("accessToken");
        if(StringUtils.isEmpty(accessToken)){
            resultError(ctx,"accessToken不能为空！");
            return false;
        }
        BaseResponse<JSONObject> appInfo = authorizationServiceFeign.getAppInfo(accessToken);
        if(!isSuccess(appInfo)){
            resultError(ctx,"无效token！");
            return false;
        }
        return true;
    }

    // 接口直接返回true 或者false
    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return false;
        }
        return true;
    }
}
