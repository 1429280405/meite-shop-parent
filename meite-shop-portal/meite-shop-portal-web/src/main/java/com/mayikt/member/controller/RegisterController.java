package com.mayikt.member.controller;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.mayikt.member.controller.req.vo.RegisterVo;
import com.mayikt.member.feign.MemberRegisterServiceFeign;
import com.mayikt.member.input.dto.UserInpDTO;
import com.mayikt.web.base.BaseWebController;
import com.mayikt.web.bean.MeiteBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@Controller
@Slf4j
public class RegisterController extends BaseWebController {
    private static final String MEMBER_REGISTER_PAGE = "member/register";

    private static final String MB_LOGIN_FTL = "member/login";

    @Autowired
    private MemberRegisterServiceFeign memberRegisterServiceFeign;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @GetMapping("/register.html")
    public String getRegister() {
        return MEMBER_REGISTER_PAGE;
    }

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @PostMapping("/register.html")
    public String postRegister(@ModelAttribute("registerVo") @Validated RegisterVo registerVo, BindingResult bindingResult, HttpSession httpSession,
                               Model model) {
        //1、验证参数
        if (bindingResult.hasErrors()) {
            //获取第一个错误
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            setErrorMsg(model, errorMsg);
            return MEMBER_REGISTER_PAGE;
        }
        //将VO转换成DTO
        UserInpDTO userInpDTO = MeiteBeanUtils.voToDto(registerVo, UserInpDTO.class);

        try {
            String registCode = registerVo.getRegistCode();
            BaseResponse<JSONObject> register = memberRegisterServiceFeign.register(userInpDTO, registCode);
            if (!register.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                model.addAttribute("error", register.getMsg());
                return MEMBER_REGISTER_PAGE;
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return MEMBER_REGISTER_PAGE;
        }
        //注册成功跳转成功页面
        return MB_LOGIN_FTL;
    }


}