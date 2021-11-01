package com.mayikt.member;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseResponse;
import com.mayikt.member.input.dto.UserInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liujinqiang
 * @create 2021-01-08 19:47
 */
@Api(tags = "会员注册接口")
public interface MemberRegisterService {


    @PostMapping("/register")
    @ApiOperation(value = "会员用户注册信息接口")
    BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO, @RequestParam("registCode") String registCode);





















}
