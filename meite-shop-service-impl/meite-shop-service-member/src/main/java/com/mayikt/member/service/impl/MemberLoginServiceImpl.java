package com.mayikt.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.token.GenerateToken;
import com.mayikt.core.transaction.RedisDataSoureceTransaction;
import com.mayikt.core.utils.MD5Util;
import com.mayikt.member.MemberLoginService;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.mapper.UserTokenMapper;
import com.mayikt.member.mapper.entity.UserDo;
import com.mayikt.member.mapper.entity.UserTokenDo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-01-10 23:07
 */
@RestController
public class MemberLoginServiceImpl extends BaseApiService<JSONObject> implements MemberLoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private GenerateToken generateToken;

    /**
     * 手动事务工具类
     */
    @Autowired
    private RedisDataSoureceTransaction manualTransaction;


    @Override
    public BaseResponse<JSONObject> login(UserLoginInpDTO userLoginInpDTO) {
        //1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空！");
        }
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登录类型不能为空！");
        }
        if (!loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) && !loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS) && !loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC)) {
            return setResultError("登陆类型错误！");
        }
        //设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空！");
        }
        //验证密码
        String newPassword = MD5Util.MD5(password);
        //用户手机号与密码查询
        UserDo userDo = this.userMapper.login(mobile, newPassword);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误！");
        }
        //验证之前是否有登录
        Long userId = userDo.getUserId();
        TransactionStatus transactionStatus = null;
        try {
            UserTokenDo userTokenDo = this.userTokenMapper.selectByUserIdAndLoginType(userId, loginType);
            transactionStatus = manualTransaction.begin();
            JSONObject jsonObject = null;

            if (userTokenDo != null) {
                //清除之前的token
                String token = userTokenDo.getToken();

                Boolean removeToken = this.generateToken.removeToken(token);
                int availability = userTokenMapper.updateTokenAvailability(userId, loginType);
                if (availability < 0) {
                    manualTransaction.rollback(transactionStatus);
                    setResultError("登录失败！");
                }
            }

            //生成新的token
            String token = generateToken.createToken(Constants.MEMBER_TOKEN_KEYPREFIX, userId + "", Constants.MEMBRE_LOGIN_TOKEN_TIME);
            jsonObject = new JSONObject();
            jsonObject.put("token", token);
            //存在数据库中
            UserTokenDo tokenDo = new UserTokenDo();
            tokenDo.setUserId(userId);
            tokenDo.setLoginType(loginType);
            tokenDo.setDeviceInfor(deviceInfor);
            tokenDo.setToken(token);
            int result = userTokenMapper.insertUserToken(tokenDo);
            if (result < 0) {
                manualTransaction.rollback(transactionStatus);
                setResultError("登录失败！");
            }
            manualTransaction.commit(transactionStatus);
            return setResultSuccess(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                manualTransaction.rollback(transactionStatus);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setResultError("登录失败！");
        }
        return setResultError("登陆失败！");
    }
}



































