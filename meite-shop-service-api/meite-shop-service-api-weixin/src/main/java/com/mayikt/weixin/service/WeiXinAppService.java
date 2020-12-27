package com.mayikt.weixin.service;

import com.mayikt.weixin.entity.AppEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liujinqiang
 * @create 2020-12-21 22:36
 */
public interface WeiXinAppService {
    /**
     * 功能说明： 应用服务接口
     */
    @GetMapping("/getApp")
    public AppEntity getApp();
}
