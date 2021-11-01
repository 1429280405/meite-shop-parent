package com.mayikt.pay.callback.template;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.pay.constant.PayConstant;
import com.mayikt.pay.mapper.PaymentTransactionLogMapper;
import com.mayikt.pay.mapper.PaymentTransactionMapper;
import com.mayikt.pay.mapper.entity.PaymentTransactionEntity;
import com.mayikt.pay.mapper.entity.PaymentTransactionLogEntity;
import com.mayikt.pay.mq.producer.IntegralProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-02-23 23:23
 */
@Slf4j
public abstract class AbstractPayCallbackTemplate {


    @Autowired
    private PaymentTransactionLogMapper paymentTransactionLogMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IntegralProducer integralProducer;

    public String asyCallBack(HttpServletRequest req, HttpServletResponse resp) {
        //1.验证报文参数 相同点 获取所有的请求参数封装成map集合 并且进行参数验证
        Map<String, String> verifySignature = verifySignature(req, resp);
        //2.将日志根据支付id存放在数据库中
        String paymentId = verifySignature.get("paymentId");
        if (StringUtils.isEmpty(paymentId)) {
            return failResult();
        }
        //3.采用异步形式写入日志到数据库中
        log.info("------------log01------------");
        threadPoolTaskExecutor.execute(new PayLogThread(paymentId,verifySignature));
        log.info("------------log04------------");
        String result = verifySignature.get(PayConstant.RESULT_NAME);
        //4.验证签名是否失败
        if (result.equals(PayConstant.RESULT_PAYCODE_201)) {
            return failResult();
        }
        //5.执行异步回调业务逻辑
        return asyncService(verifySignature);

    }

    public abstract String asyncService(Map<String, String> verifySignature);

    private void payLog(String paymentId, Map<String, String> verifySignature) {
        log.info(">>paymentId:{paymentId},verifySignature:{}", verifySignature);
        PaymentTransactionLogEntity paymentTransactionLog = new PaymentTransactionLogEntity();
        paymentTransactionLog.setTransactionId(paymentId);
        paymentTransactionLog.setAsyncLog(verifySignature.toString());
        paymentTransactionLogMapper.insertTransactionLog(paymentTransactionLog);
    }

    public abstract String failResult();

    public abstract String successResult();

    public abstract Map<String, String> verifySignature(HttpServletRequest req, HttpServletResponse resp);

    class PayLogThread implements Runnable {

        private String paymentId;
        private Map<String, String> verifySignature;

        public PayLogThread(String paymentId, Map<String, String> verifySignature) {
            this.paymentId = paymentId;
            this.verifySignature = verifySignature;
        }


        @Override
        public void run() {
            log.info("------------log02------------");
            payLog(paymentId, verifySignature);
            log.info("------------log03------------");
        }
    }


    /**
     * 增加积分
     *
     * @param paymentTransaction
     */
    @Async
    protected void addIntegral(PaymentTransactionEntity paymentTransaction) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paymentId", paymentTransaction.getPaymentId());
        jsonObject.put("userId", paymentTransaction.getUserId());
        jsonObject.put("integral", 100);
        jsonObject.put("paymentChannel", paymentTransaction.getPaymentChannel());
        integralProducer.send(jsonObject);
    }

}
