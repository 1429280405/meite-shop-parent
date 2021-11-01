package com.mayikt.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.auth.mapper.AppInfoMapper;
import com.mayikt.auth.mapper.entity.MeiteAppInfo;
import com.mayikt.auth.service.api.AuthorizationService;
import com.mayikt.auth.util.Guid;
import com.mayikt.core.token.GenerateToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-03-03 22:05
 */
@RestController
public class AuthorizationServiceImpl extends BaseApiService<JSONObject> implements AuthorizationService {

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> applyAppInfo(String appName) {
        //1.验证参数
        if (StringUtils.isEmpty(appName)) {
            return setResultError("机构名称不能为空");
        }
        //2.生成appid和appsecret
        Guid guid = new Guid();
        String appId = guid.getAppId();
        String appScrect = guid.getAppScrect();
        //3.添加至数据库中
        MeiteAppInfo meiteAppInfo = new MeiteAppInfo(appName, appId, appScrect);
        int result = appInfoMapper.insertAppInfo(meiteAppInfo);
        if (!toDaoResult(result)) {
            return setResultError("申请失败");
        }
        //4.返回给用户
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId", appId);
        jsonObject.put("appScrect", appScrect);
        return setResultSuccess(jsonObject);
    }

    @Override
    public BaseResponse<JSONObject> getAccessToken(String appId, String appSecret) {
        //验证参数
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret)){
            return setResultError("请求参数错误！");
        }
        //通过参数去库里查询机构
        MeiteAppInfo meiteAppInfo = appInfoMapper.selectByAppInfo(appId, appSecret);
        if(meiteAppInfo == null){
            return setResultError("机构id或者秘钥错误！");
        }
        //如果存在机构，生成token，放入redis
        String token = generateToken.createToken("auth", meiteAppInfo.getAppId());
        //返回给用户token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        return setResultSuccess(jsonObject);
    }

    @Override
    public BaseResponse<JSONObject> getAppInfo(String accessToken) {
        //验证参数
        if(StringUtils.isEmpty(accessToken)){
            return setResultError("token不能为空！");
        }
        //通过token去redis中获取appid
        String appid = generateToken.getToken(accessToken);
        //验证token是否过期，从数据库中查询机构
        if(StringUtils.isEmpty(appid)){
            return setResultError("token已过期！");
        }
        MeiteAppInfo appInfo = appInfoMapper.findByAppInfo(appid);
        //返回机构给用户
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId",appInfo.getAppId());
        jsonObject.put("appSecret",appInfo.getAppSecret());
        return setResultSuccess(jsonObject);
    }
}
