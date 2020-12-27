package com.mayikt.member.service.impl;

import com.mayikt.member.MemberService;
import com.mayikt.member.feign.WeiXinAppServiceFeign;
import com.mayikt.weixin.entity.AppEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2020-12-21 22:46
 */
@RestController
public class MemberServiceImpl implements MemberService {

    @Autowired
    private WeiXinAppServiceFeign weiXinAppServiceFeign;

    @Override
    @GetMapping("memberInvokeWeixin")
    public AppEntity memberInvokeWeixin() {
        return weiXinAppServiceFeign.getApp();
    }
}
