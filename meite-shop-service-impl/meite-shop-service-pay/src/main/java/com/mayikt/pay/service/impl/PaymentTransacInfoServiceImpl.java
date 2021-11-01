package com.mayikt.pay.service.impl;

import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.core.token.GenerateToken;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.mayikt.pay.service.PayMentTransacInfoService;
import com.mayikt.weixin.out.dto.PayMentTransacDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujinqiang
 * @create 2021-02-19 23:16
 */
@RestController
public class PaymentTransacInfoServiceImpl extends BaseApiService<PayMentTransacDTO>
    implements PayMentTransacInfoService {

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;


    @Override
    public BaseResponse<PayMentTransacDTO> tokenByPayMentTransac(String token) {
        //验证token是否为空
        if(StringUtils.isEmpty(token)){
            return setResultError("token参数不能为空！");
        }
        //使用token查询redis
        String value = generateToken.getToken(token);
        if(StringUtils.isEmpty(value)){
            return setResultError("该token可能已经失效或者过期！");
        }
        //转换为整数类型
        long transactionId = Long.parseLong(value);
        //使用transactionId查询支付信息
        PaymentTransactionEntity transactionEntity = paymentTransactionMapper.selectById(transactionId);
        if(transactionEntity == null){
            return setResultError("未查询到该支付信息");
        }

        return setResultSuccess(MiteBeanUtils.doToDto(transactionEntity,PayMentTransacDTO.class));
    }
}
