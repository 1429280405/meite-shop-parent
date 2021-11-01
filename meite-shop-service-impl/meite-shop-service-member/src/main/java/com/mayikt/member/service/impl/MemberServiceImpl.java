package com.mayikt.member.service.impl;

import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.core.token.GenerateToken;
import com.mayikt.core.type.TypeCastHelper;
import com.mayikt.core.utils.MD5Util;
import com.mayikt.member.MemberService;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.entity.UserDo;
import com.mayikt.member.output.dto.UserOutDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2020-12-21 22:46
 */
@RestController
public class MemberServiceImpl extends BaseApiService<UserOutDTO> implements MemberService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<UserOutDTO> existMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空");
        }
        UserDo userDo = userMapper.existMobile(mobile);
        //将DO转换成DTO
        UserOutDTO userOutDTO = MiteBeanUtils.doToDto(userDo, UserOutDTO.class);
        if (userOutDTO == null) {
            return setResultError(Constants.HTTP_RES_CODE_202, "用户不存在");
        }

        return setResultSuccess(userOutDTO);
    }

    @Override
    public BaseResponse<UserOutDTO> getInfo(String token) {
        if (StringUtils.isEmpty(token)) {
            return setResultError("token不能为空！");
        }
        //2、使用token查询userId
        String redisValue = generateToken.getToken(token);
        if (StringUtils.isEmpty(redisValue)) {
            return setResultError("token已经失效或者不正确！");
        }
        long userId = TypeCastHelper.toLong(redisValue);
        //根据userId查询用户信息
        UserDo userDo = userMapper.findByUserId(userId);
        if (userDo == null) {
            return setResultError("用户信息不存在！");
        }
        UserOutDTO userOutDTO = MiteBeanUtils.doToDto(userDo, UserOutDTO.class);
        return setResultSuccess(userOutDTO);
    }

// token存放在PC端 cookie token 存放在安卓 或者IOS端 存放在本地文件中
    // 当前存在那些问题？ 用户如果退出或者修改密码、忘记密码的情况 对token状态进行标识
    // token 如何防止伪造 真正其实很难防御伪造 尽量实现在安全体系 xss 只能在一些某些业务模块上加上必须验证本人操作

    @Override
    public BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        // 1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }
        // 2.对登陆密码实现加密
        String newPassWord = MD5Util.MD5(password);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        UserDo userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误!");
        }
        return setResultSuccess(MiteBeanUtils.doToDto(userDo, UserOutDTO.class));
    }
}
