package com.flower.mianshiflower.manager;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 通用计数器
 */
@Service
@Slf4j
public class CounterManager {

    @Resource
    private RedissonClient redissonClient;


    /**
     * 最简化版本，默认时间间隔1分钟
     * @param key
     * @return
     */
    public long incrAndGet(String key) {
        return incrAndGet(key, 1, TimeUnit.MINUTES);
    }


    /**
     * 无需传入过期时间版本
     * @param key
     * @param intervalTime
     * @param timeUnit
     * @return
     */
    public long incrAndGet(String key, int intervalTime, TimeUnit timeUnit) {
        long expireTime;
        switch (timeUnit) {
            case SECONDS:
                expireTime = intervalTime;
                break;
            case MINUTES:
                expireTime = intervalTime * 60L;
                break;
            case HOURS:
                expireTime = intervalTime * 60L * 60;
                break;
            default:
                throw new IllegalArgumentException("不支持的单位：" + timeUnit);
        }
        return incrAndGet(key, intervalTime, timeUnit, expireTime);
    }


    /**
     * 增加并返回计数
     *
     * @param key
     * @param intervalTime
     * @param timeUnit
     * @param expireTime
     * @return
     */
    public long incrAndGet(
            String key, int intervalTime, TimeUnit timeUnit, long expireTime
    ) {
        if (StrUtil.isBlank(key)) {
            return 0;
        }
        //根据粒度生成时间因子
        long timeFactor;
        long curSeconds = Instant.now().getEpochSecond();
        switch (timeUnit) {
            case SECONDS:
                timeFactor = curSeconds / intervalTime;
                break;
            case MINUTES:
                timeFactor = curSeconds / intervalTime / 60;
                break;
            case HOURS:
                timeFactor = curSeconds / intervalTime / 60 / 60;
                break;
            default:
                throw new IllegalArgumentException("不支持的时间单位");
        }
        String redisKey = key + ":" + timeFactor;
        //Lua脚本
        String luaScript =
                "if redis.call('exists', KEYS[1]) == 1 then " +
                        "  return redis.call('incr', KEYS[1]); " +
                        "else " +
                        "  redis.call('set', KEYS[1], 1); " +
                        "  redis.call('expire', KEYS[1], 180); " +  // 设置 180 秒过期时间
                        "  return 1; " +
                        "end";
        // 执行 Lua 脚本
        RScript script = redissonClient.getScript(IntegerCodec.INSTANCE);
        Object countObj = script.eval(
                RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.INTEGER,
                Collections.singletonList(redisKey),
                expireTime
        );
        return (long) countObj;
    }
}
