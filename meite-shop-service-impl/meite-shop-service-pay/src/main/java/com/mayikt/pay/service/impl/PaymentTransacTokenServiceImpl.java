package com.mayikt.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.core.token.GenerateToken;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.mayikt.pay.service.PayMentTransacTokenService;
import com.mayikt.twitter.SnowflakeIdUtils;
import com.mayikt.weixin.input.dto.PayCratePayTokenDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-02-19 23:01
 */
@RestController
public class PaymentTransacTokenServiceImpl extends BaseApiService<JSONObject>
        implements PayMentTransacTokenService {

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private GenerateToken generateToken;

    @Override
    public BaseResponse<JSONObject> cratePayToken(PayCratePayTokenDto payCratePayTokenDto) {
        if (StringUtils.isEmpty(payCratePayTokenDto.getOrderId())) {
            return setResultError("订单号码不能为空");
        }
        if (payCratePayTokenDto.getPayAmount() == null) {
            return setResultError("金额不能为空！");
        }
        if (payCratePayTokenDto.getUserId() == null) {
            return setResultError("userId不能为空！");
        }

        //将输入插入数据库中，待支付记录
        PaymentTransactionEntity transactionEntity = new PaymentTransactionEntity();
        transactionEntity.setOrderId(payCratePayTokenDto.getOrderId());
        transactionEntity.setPayAmount(payCratePayTokenDto.getPayAmount());
        transactionEntity.setUserId(payCratePayTokenDto.getUserId());

        //使用雪花算法，生成全局id
        transactionEntity.setPaymentId(SnowflakeIdUtils.nextId());
        int result = paymentTransactionMapper.insertPaymentTransaction(transactionEntity);
        if (!toDaoResult(result)) {
            return setResultError("系统错误！");
        }
        //获取主键id
        Long payId = transactionEntity.getId();
        if (payId == null) {
            return setResultError("系统错误！");
        }

        //生成对应支付令牌
        String keyPrefix = "pay_";
        String token = generateToken.createToken(keyPrefix, payId + "");
        JSONObject dataResult = new JSONObject();
        dataResult.put("token", token);

        return setResultSuccess(dataResult);
    }
}
