package com.mayikt.zuul.feign;

import com.mayikt.auth.service.api.AuthorizationService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2021-03-03 22:50
 */
@FeignClient("app-mayikt-auth")
public interface AuthorizationServiceFeign extends AuthorizationService {
}
