package com.mayikt.constants;

public interface Constants {
	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "success";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "fail";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;
	// 未关联QQ账号
	Integer HTTP_RES_CODE_201 = 201;
	// 用户不存在
	Integer HTTP_RES_CODE_202 = 202;
	// 发送邮件
	String MSG_EMAIL = "email";
	// 会员token
	String TOKEN_MEMBER = "TOKEN_MEMBER";
	// 用户有效期 90天
	Long TOKEN_MEMBER_TIME = (long) (60 * 60 * 24 * 90);
	int COOKIE_TOKEN_MEMBER_TIME = (60 * 60 * 24 * 90);
	// cookie 会员 totoken 名称
	String COOKIE_MEMBER_TOKEN = "cookie_member_token";
	// 微信code
	String WEIXINCODE_KEY = "weixin.code";
	// 微信注册码有效期30分钟
	Long WEIXINCODE_TIMEOUT = 1800l;

	// token
	String MEMBER_TOKEN_KEYPREFIX = "mayikt.member.login";

	// 安卓的登陆类型
	String MEMBER_LOGIN_TYPE_ANDROID = "Android";
	// IOS的登陆类型
	String MEMBER_LOGIN_TYPE_IOS = "IOS";

	// PC的登陆类型
	String MEMBER_LOGIN_TYPE_PC = "PC";

	// 登陆超时时间 有效期 90天
	Long MEMBRE_LOGIN_TOKEN_TIME = 77776000L;

}