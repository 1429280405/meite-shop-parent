package com.mayikt.member.feign;

import com.mayikt.weixin.service.WeiXinAppService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2020-12-21 22:45
 */
@FeignClient("app-mayikt-weixin")
public interface WeiXinAppServiceFeign extends WeiXinAppService {
}
