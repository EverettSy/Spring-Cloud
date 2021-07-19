package com.syraven.cloud.configuration.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

/**
 * 布隆过滤器配置类，因为本项目主要是url去重，所以默认是String类型的数据
 *
 * @author SyRAVEN
 * @since 2021-07-08 19:46
 */
public class BloomFilterHelper {

    /**
     * hash 次数
     */
    private int numHashFunctions;

    /**
     * 布隆过滤器大小
     */
    private long bitSize;

    /**
     * 过滤器名称
     */
    private String key;

    /**
     * @param bfKey
     * @param expectedInsertions
     * @param fpp
     */
    public BloomFilterHelper(String bfKey, int expectedInsertions, double fpp) {
        this.key = bfKey;
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    /**
     * 计算value在bit数组中的所有位置
     *
     * @param value
     * @return
     */
    long[] murmurHashOffset(String value) {
        long[] offset = new long[numHashFunctions];

        byte[] bytes = Hashing.murmur3_128().hashString(value, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        for (int i = 0; i <= numHashFunctions; i++) {
            long nexHash = hash1 + i * hash2;
            if (nexHash < 0) {
                nexHash = ~nexHash;
            }
            offset[i - 1] = nexHash % bitSize;
        }
        return offset;

    }


    /**
     * 返回过滤器的名称
     *
     * @return
     */
    public String getBfKey() {
        return this.key;
    }

    private long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private long upperEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }

    /**
     * 计算bit数组长度
     *
     * @param n
     * @param p
     * @return
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     *
     * @param n
     * @param m
     * @return
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
