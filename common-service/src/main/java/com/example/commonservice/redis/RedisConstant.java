package com.example.commonservice.redis;

public class RedisConstant {
    public final static int MAX_REQUEST = 5;
    public final static int WINDOW_SECONDS = 1;
    public final static String BLACKLIST_KEY = "user:blacklist";
    public final static String BLACKLIST_REASON_KEY = "user:blacklist:reason:";
    public final static String RANKING_KEY = "dish:sales:ranking";
    public final static String SECKILL_STOCK_KEY = "seckill:stock";
    public final static String SECKILL_USERS_KEY = "seckill:users";
}
