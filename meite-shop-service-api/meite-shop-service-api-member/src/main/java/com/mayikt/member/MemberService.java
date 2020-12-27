package com.mayikt.member;

import com.mayikt.weixin.entity.AppEntity;

/**
 * @author liujinqiang
 * @create 2020-12-21 21:57
 */
public interface MemberService {
    /**
     * 会员服务接口调用微信接口
     *
     * @return
     */
    public AppEntity memberInvokeWeixin();
}
