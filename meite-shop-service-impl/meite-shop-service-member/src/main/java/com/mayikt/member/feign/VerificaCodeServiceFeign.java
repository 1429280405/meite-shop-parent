package com.mayikt.member.feign;

import com.mayikt.weixin.service.VerificaCodeService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author liujinqiang
 * @create 2021-01-08 20:04
 */
@FeignClient("app-mayikt-weixin")
public interface VerificaCodeServiceFeign extends VerificaCodeService {

}
