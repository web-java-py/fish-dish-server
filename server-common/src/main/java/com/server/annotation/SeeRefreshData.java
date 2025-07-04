package com.server.annotation;

import com.server.enums.SeeMessageType;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeeRefreshData {

    /**
     * 消息类型
     */
    public SeeMessageType seeMessageType() default SeeMessageType.ALL;
}
