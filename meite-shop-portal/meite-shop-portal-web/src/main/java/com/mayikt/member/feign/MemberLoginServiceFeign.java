package com.mayikt.member.feign;

import com.mayikt.member.MemberLoginService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2021-01-28 23:07
 */
@FeignClient("app-mayikt-member")
public interface MemberLoginServiceFeign extends MemberLoginService {
}
