package com.mayikt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.member.controller.req.vo.LoginVo;
import com.mayikt.member.feign.MemberLoginServiceFeign;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.web.base.BaseWebController;
import com.mayikt.web.bean.MeiteBeanUtils;
import com.mayikt.web.utils.CookieUtils;
import com.mayikt.web.utils.RandomValidateCodeUtil;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author liujinqiang
 * @create 2021-01-11 21:52
 */
@Controller
public class LoginController extends BaseWebController {
    private static final String MEMBER_LOGIN_PAGE = "member/login";

    /**
     * 重定向到首页
     */
    private static final String REDIRECT_INDEX = "redirect:/";


    @Autowired
    private MemberLoginServiceFeign memberLoginServiceFeign;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @GetMapping("/login.html")
    public String getLogin() {
        return MEMBER_LOGIN_PAGE;
    }

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @PostMapping("/login.html")
    public String postLogin(@ModelAttribute("loginVo") LoginVo loginVo, Model model, HttpServletResponse response,
                            HttpServletRequest request, HttpSession httpSession) {
        //1、验证图形验证码
        String graphicCode = loginVo.getGraphicCode();
        if(!RandomValidateCodeUtil.checkVerify(graphicCode,httpSession)){
            setErrorMsg(model,"图形验证码不正确！");
            return MEMBER_LOGIN_PAGE;
        }
        //2、将vo转成dto
        UserLoginInpDTO loginInpDTO = MeiteBeanUtils.voToDto(loginVo, UserLoginInpDTO.class);
        loginInpDTO.setLoginType(Constants.MEMBER_LOGIN_TYPE_PC);
        String info = webBrowserInfo(request);
        loginInpDTO.setDeviceInfor(info);
        BaseResponse<JSONObject> login = memberLoginServiceFeign.login(loginInpDTO);
        if(!isSuccess(login)){
            setErrorMsg(model,login.getMsg());
            return MEMBER_LOGIN_PAGE;
        }
        //3.将token放入cookie中
        JSONObject jsonObject = login.getData();
        String token = jsonObject.getString("token");
        CookieUtils.setCookie(request,response,"meite_token",token);
        // 登陆成功后，跳转到首页
        return REDIRECT_INDEX;
    }










}















































