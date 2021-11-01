package com.mayikt.member;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseResponse;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author liujinqiang
 * @create 2021-01-10 22:59
 */
@Api(tags = "用户登陆服务接口")
public interface MemberLoginService {
    /**
     * 用户登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);

}


















