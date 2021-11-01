package com.mayikt.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.weixin.service.VerificaCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-01-07 23:06
 */
@RestController
public class VerificaCodeServiceImpl extends BaseApiService<JSONObject> implements VerificaCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BaseResponse<JSONObject> verificaWeixinCode(String phone, String weixinCode) {
        if (StringUtils.isBlank(phone)) {
            return setResultError("手机号不能为空！");
        }
        if (StringUtils.isBlank(weixinCode)) {
            return setResultError("验证码不能为空！");
        }
        String redisCode = redisUtil.getString(Constants.WEIXINCODE_KEY + phone);
        if (StringUtils.isBlank(redisCode)) {
            return setResultError("验证码已经过期，请重新发送验证码！");
        }
        if (!redisCode.equals(weixinCode)) {
            return setResultError("验证码错误，请重试！");
        }

        return setResultSuccess("注册验证码正确！");
    }
}


























