package com.mayikt.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.pay.factory.StrategyFactory;
import com.mayikt.pay.mapper.PaymentChannelMapper;
import com.mayikt.pay.mapper.entity.PaymentChannelEntity;
import com.mayikt.pay.service.PayContextService;
import com.mayikt.pay.service.PayMentTransacInfoService;
import com.mayikt.pay.strategy.PayStrategy;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-02-20 21:46
 */
@RestController
public class PayContextServiceImpl extends BaseApiService<JSONObject>
    implements PayContextService {

    @Autowired
    private PaymentChannelMapper paymentChannelMapper;

    @Autowired
    private PayMentTransacInfoService payMentTransacInfoService;

    @Override
    public BaseResponse<JSONObject> toPayHtml(String channelId, String payToken) {
        //使用渠道id获取渠道信息
        PaymentChannelEntity channelEntity = paymentChannelMapper.selectBychannelId(channelId);
        if(channelEntity == null){
            return setResultError("没有该渠道信息！");
        }
        //2、使用payToken获取支付参数j
        BaseResponse<PayMentTransacDTO> payMentTransacDTOBaseResponse = payMentTransacInfoService.tokenByPayMentTransac(payToken);
        if(!isSuccess(payMentTransacDTOBaseResponse)){
            return setResultError(payMentTransacDTOBaseResponse.getMsg());
        }
        //3、执行具体的支付渠道的算法获取html表单数据 策略设计模式 使用java反射机制 执行具体方法
        PayMentTransacDTO data = payMentTransacDTOBaseResponse.getData();
        String classAddres = channelEntity.getClassAddres();
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(classAddres);

        //4、直接返回html
        String payHtml = payStrategy.toPayHtml(channelEntity, data);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payHtml",payHtml);
        return setResultSuccess(jsonObject);
    }
}
