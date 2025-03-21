package com.flower.mianshiflower.blackfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * 黑名单过滤器
 */
@Slf4j
public class BlackIpUtils {

    private static BitMapBloomFilter bitMapBloomFilter;

    //判断ip是否在黑名单内
    public static boolean isBlackIp(String ip) {
        return bitMapBloomFilter.contains(ip);
    }

    /**
     * 重建黑名单
     * @param configInfo
     */
    public static void rebuildBlackIp(String configInfo) {
        if (StrUtil.isBlank(configInfo)) {
            configInfo = "{}";
        }
        //解析yaml
        Yaml yaml = new Yaml();
        Map map = yaml.loadAs(configInfo, Map.class);
        List<String> blackIpList = (List<String>) map.get("blackIpList");
        //加锁防止并发
        synchronized (BlackIpUtils.class) {
            if (CollUtil.isNotEmpty(blackIpList)) {
                BitMapBloomFilter bloomFilter = new BitMapBloomFilter(958506);
                for (String blackIp : blackIpList) {
                    bloomFilter.add(blackIp);
                }
                bitMapBloomFilter = bloomFilter;
            } else {
                bitMapBloomFilter = new BitMapBloomFilter(100);
            }
        }
    }
}
