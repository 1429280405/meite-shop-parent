package com.mayikt.zuul.handler;

import com.mayiket.base.BaseResponse;
import com.mayikt.constants.Constants;
import com.netflix.zuul.context.RequestContext;

public class BaseHandler {
	protected GatewayHandler gatewayHandler;

	public void setNextHandler(GatewayHandler gatewayHandler) {
		this.gatewayHandler = gatewayHandler;
	}

	public GatewayHandler getNextHandler() {
		return gatewayHandler;
	}

	protected void resultError(RequestContext ctx, String errorMsg) {
		ctx.setResponseStatusCode(401);
		// 网关响应为false 不会转发服务
		ctx.setSendZuulResponse(false);
		ctx.setResponseBody(errorMsg);
	}

	// 接口直接返回true 或者false
	public Boolean isSuccess(BaseResponse<?> baseResp) {
		if (baseResp == null) {
			return false;
		}
		if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			return false;
		}
		return true;
	}

}
