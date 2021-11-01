package com.mayikt.member.feign;

import com.mayikt.member.MemberRegisterService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2021-01-12 20:20
 */
@FeignClient("app-mayikt-member")
public interface MemberRegisterServiceFeign extends MemberRegisterService {
}
