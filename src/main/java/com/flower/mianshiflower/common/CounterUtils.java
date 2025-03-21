package com.flower.mianshiflower.common;

import cn.dev33.satoken.stp.StpUtil;
import com.flower.mianshiflower.exception.BusinessException;
import com.flower.mianshiflower.manager.CounterManager;
import com.flower.mianshiflower.model.entity.User;
import com.flower.mianshiflower.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 计数器工具类
 */
@Service
public class CounterUtils {

    @Resource
    private CounterManager counterManager;

    @Resource
    private UserService userService;

    /**
     * 检测爬虫
     *
     * @param loginUserId
     */
    public void crawlerDetect(long loginUserId) {
        // 调用多少次告警
        final int WARN_COUNT = 10;
        // 调用多少次封号
        final int BAN_COUNT = 20;
        // 拼接访问key
        String key = String.format("user:access:%s", loginUserId);
        // 统计用户一分钟访问次数，180s过期
        long count = counterManager.incrAndGet(key, 1, TimeUnit.MINUTES, 180);
        // 判断是否封号
        if (count > BAN_COUNT) {
            // 踢下线
            StpUtil.kickout(loginUserId);
            // 封号
            User update = new User();
            update.setId(loginUserId);
            update.setUserRole("ban");
            userService.updateById(update);
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您的账号已被封禁，请联系管理员");
        }
        // 是否告警
        if (count == WARN_COUNT) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您的账号访问次数过多，请稍后再试");
        }
    }
}
