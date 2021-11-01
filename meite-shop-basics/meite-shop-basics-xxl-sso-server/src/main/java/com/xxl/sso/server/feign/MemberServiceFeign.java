package com.xxl.sso.server.feign;

import com.mayikt.member.MemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2021-02-04 22:06
 */
@FeignClient("app-mayikt-member")
public interface MemberServiceFeign extends MemberService {
}
