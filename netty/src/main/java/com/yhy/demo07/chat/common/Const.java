package com.yhy.demo07.chat.common;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Const {

    public static final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

    public static final MessageCodec messageCodec = new MessageCodec();

    public static String getCurrentMethodMessage(Class cls) {
        return "方法" + Thread.currentThread().getStackTrace()[1].getMethodName();
    }
}
