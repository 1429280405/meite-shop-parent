package com.mayikt.weixin.service.impl;

import com.mayikt.weixin.entity.AppEntity;
import com.mayikt.weixin.service.WeiXinAppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2020-12-21 22:41
 */
@RestController
public class WeiXinAppServiceImpl implements WeiXinAppService {

    @Override
    @GetMapping("getApp")
    public AppEntity getApp() {
        return new AppEntity("123456","应用宝");
    }
}
