package com.iqiongzhi.SCB.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String appId = dotenv.get("APP_ID");
    public static final String appSecret = dotenv.get("APP_SECRET");
}
