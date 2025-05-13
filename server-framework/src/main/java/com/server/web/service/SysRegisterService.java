package com.server.web.service;

import com.server.constant.CacheConstants;
import com.server.constant.Constants;
import com.server.constant.UserConstants;
import com.server.core.domain.entity.SysUser;
import com.server.core.domain.model.RegisterBody;
import com.server.core.redis.RedisCache;
import com.server.exception.user.CaptchaException;
import com.server.exception.user.CaptchaExpireException;
import com.server.utils.MessageUtils;
import com.server.utils.SecurityUtils;
import com.server.utils.StringUtils;
import com.server.manager.AsyncManager;
import com.server.manager.factory.AsyncFactory;
import com.server.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 注册校验方法
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        String cacheCode = redisCache.getCacheObject(CacheConstants.MAIL_CODE_KEY + registerBody.getEmail()); // 获取缓存中该账号的验证码
        if (cacheCode == null) {
            msg = MessageUtils.message("user.captcha.expired");
        } else if (!cacheCode.equals(registerBody.getVerifyCode())) {
            msg = MessageUtils.message("user.captcha.invalid");
        } else if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        }
        if (!ObjectUtils.isEmpty(userService.selectUserByUserName(username))) {
            msg = "用户名已存在";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!password.equals(registerBody.getConfirmPassword())) {
            msg = "两次密码不正确";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            sysUser.setEmail(registerBody.getEmail());
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
