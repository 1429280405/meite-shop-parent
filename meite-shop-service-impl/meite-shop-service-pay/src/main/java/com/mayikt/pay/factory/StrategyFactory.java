package com.mayikt.pay.factory;

import com.mayikt.pay.strategy.PayStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liujinqiang
 * @create 2021-02-20 22:01
 */
public class StrategyFactory {
    private static Map<String,PayStrategy> strategyMap = new ConcurrentHashMap<String,PayStrategy>();
    public static PayStrategy getPayStrategy(String classAddres){
        Class<PayStrategy> payStrategy = null;
        try {
            PayStrategy strategy = strategyMap.get(classAddres);
            if(strategy != null){
                return strategy;
            }
            payStrategy=(Class<PayStrategy>) Class.forName(classAddres);
            PayStrategy instance = payStrategy.newInstance();
            strategyMap.put(classAddres,instance);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
