package com.server.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.server.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        if (ObjectUtils.isNotEmpty(SecurityUtils.getUsername())) {
            this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtils.getUsername());
            this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        if (ObjectUtils.isNotEmpty(SecurityUtils.getUsername())) {
            this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
        }
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
