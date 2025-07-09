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
        String username;
        try {
            username = SecurityUtils.getUsername();
            if (ObjectUtils.isEmpty(username)) {
                username = "system";
            }
        } catch (Exception e) {
            username = "system";
        }
        this.strictInsertFill(metaObject, "createBy", String.class, username);
        this.strictInsertFill(metaObject, "updateBy", String.class, username);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
    // 有用户时，正常获取用户名。
    // 无用户或抛异常时，自动填充为 "system"。
    // 不会影响正常登录用户的填充，也不会因为没有用户而报错
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        String username;
        try {
            username = SecurityUtils.getUsername();
            if (ObjectUtils.isEmpty(username)) {
                username = "system";
            }
        } catch (Exception e) {
            username = "system";
        }
        this.strictUpdateFill(metaObject, "updateBy", String.class, username);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
