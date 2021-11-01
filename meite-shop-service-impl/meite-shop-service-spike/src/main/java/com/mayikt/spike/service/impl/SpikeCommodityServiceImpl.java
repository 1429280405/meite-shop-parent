package com.mayikt.spike.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.mayiket.base.BaseApiService;
import com.mayiket.base.BaseResponse;
import com.mayikt.api.spike.service.SpikeCommodityService;
import com.mayikt.core.token.GenerateToken;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.spike.producer.SpikeCommodityProducer;
import com.mayikt.spike.service.mapper.OrderMapper;
import com.mayikt.spike.service.mapper.SeckillMapper;
import com.mayikt.spike.service.mapper.entity.SeckillEntity;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author liujinqiang
 * @create 2021-03-10 16:40
 */
@RestController
@Slf4j
public class SpikeCommodityServiceImpl extends BaseApiService<JSONObject> implements SpikeCommodityService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GenerateToken generateToken;

    @Autowired
    private SpikeCommodityProducer spikeCommodityProducer;

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    @Transactional
    @HystrixCommand(fallbackMethod = "spikeFallback")//开启线程池隔离，解决服务血崩问题
    public BaseResponse<JSONObject> spike(String phone, Long seckillId) {
        log.info("秒杀接口spike,线程池名称：{}", Thread.currentThread().getName());
        //用户限流频率设置 每秒中限制1个请求
        boolean tryAcquire = rateLimiter.tryAcquire(0, TimeUnit.SECONDS);
        if (!tryAcquire) {
            return setResultError("服务忙，请稍后重试！");
        }
        //1、参数验证
        if (StringUtils.isEmpty(phone)) {
            return setResultError("手机号不能为空！");
        }
        if (seckillId == null) {
            return setResultError("商品id不能为空！");
        }
        String token = generateToken.getListKeyToken(seckillId + "");
        if (StringUtils.isEmpty(token)) {
            return setResultError("商品库存已经售空啦！请关注下一次机会哦！");
        }
        addMqSpike(seckillId, phone);
        return setResultSuccess("正在派对中。。。");
    }

    private BaseResponse<JSONObject> spikeFallback(String phone, Long seckillId) {
        return setResultError("服务器忙,请稍后重试!");
    }

    @Async
    void addMqSpike(Long seckillId, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("phone", phone);
        spikeCommodityProducer.send(jsonObject);
    }

    @Override
    public BaseResponse<JSONObject> addSpikeToken(Long seckillId, Long tokenQuantity) {
        //验证参数
        if (seckillId == null) {
            return setResultError("商品id不能为空！");
        }
        if (tokenQuantity == null) {
            return setResultError("商品数量不能为空！");
        }
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return setResultError("商品不存在！");
        }
        //多线程异步生产令牌
        createSeckilToken(seckillId, tokenQuantity);
        return setResultSuccess("商品生成中。。。");
    }

    @Async
    void createSeckilToken(Long seckillId, Long tokenQuantity) {
        generateToken.createListToken("seckill_", seckillId + "", tokenQuantity);
    }
}
