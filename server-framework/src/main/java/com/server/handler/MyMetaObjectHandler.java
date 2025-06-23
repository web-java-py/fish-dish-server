package com.server.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.server.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.server.utils.SecurityUtils.getLoginUser;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        try {
            if (getFieldValByName("updateBy", metaObject) == null) {
                this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
            }
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            if (getFieldValByName("createBy", metaObject) == null) {
                this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtils.getUsername());
            }
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        } catch (Exception e) {
            // 使用默认值或系统用户
            if (getFieldValByName("updateBy", metaObject) == null) {
                this.strictInsertFill(metaObject, "updateBy", String.class, "system");
            }
            if (getFieldValByName("createBy", metaObject) == null) {
                this.strictInsertFill(metaObject, "createBy", String.class, "system");
            }
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        try {
            if (getFieldValByName("updateBy", metaObject) == null) {
                this.strictUpdateFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
            }
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        } catch (Exception e) {
            // 使用默认值或系统用户
            if (getFieldValByName("updateBy", metaObject) == null) {
                this.strictUpdateFill(metaObject, "updateBy", String.class, "system");
            }
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
