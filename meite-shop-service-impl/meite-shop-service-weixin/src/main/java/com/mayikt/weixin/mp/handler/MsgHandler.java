package com.mayikt.weixin.mp.handler;


import com.mayikt.constants.Constants;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.core.utils.RegexUtils;
import com.mayikt.weixin.mp.builder.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
@Slf4j
public class MsgHandler extends AbstractHandler {

	@Value("${mayikt.weixin.registration.code.message}")
	private String registrationCodeMessage;

	@Value("${mayikt.weixin.default.registration.code.message}")
	private String defaultRegistrationCodeMessage;

	@Autowired
	private RedisUtil redisUtil;


	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) {

		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			// TODO 可以选择将消息保存到本地
		}

		// 当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
		try {
			if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
					&& weixinService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
				return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
						.toUser(wxMessage.getFromUser()).build();
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		String fromContent = wxMessage.getContent();
		System.out.println("fromContent:" + fromContent);
		if(RegexUtils.checkMobile(fromContent)){
			//如果发送消息为手机号码类型，则发送短信验证码
			//生成4位验证码
			int registCode = registCode();
			String content = String.format(registrationCodeMessage, registCode);
			//将验证码存放在redis中
			redisUtil.setString(Constants.WEIXINCODE_KEY + fromContent,registCode+"",Constants.WEIXINCODE_TIMEOUT);
			return new TextBuilder().build(content, wxMessage, weixinService);
		}

		return new TextBuilder().build(defaultRegistrationCodeMessage, wxMessage, weixinService);

	}

	private int registCode() {
		int registCode = (int)(Math.random() * 9000 + 1000);
		return registCode;
	}

}
