package com.server.web.service;

import com.server.core.redis.RedisCache;
import com.server.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.server.constant.Constants;
import com.server.constant.CacheConstants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class MailService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUserName;

    /**
     * 获取邮箱验证码
     *
     * @param mailAddress 用户邮箱
     * @return
     */
    public String getCode(String mailAddress) {
        String key = CacheConstants.MAIL_CODE_KEY + mailAddress;
        String verifyCode = redisCache.getCacheObject(key);
        if (StringUtils.isNotEmpty(verifyCode)) {
            redisCache.deleteObject(key);
        }
        verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);//生成短信验证码
        // 验证码存入redis并设置过期时间
        redisCache.setCacheObject(key, verifyCode, Constants.MAIL_EXPIRATION, TimeUnit.MINUTES);
        // 编写邮箱内容
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head><title></title></head><body>");
        stringBuilder.append("您好<br/>");
        stringBuilder.append("您的验证码是：").append(verifyCode).append("<br/>");
        stringBuilder.append("您可以复制此验证码并返回至管理系统，以验证您的邮箱。<br/>");
        stringBuilder.append("此验证码只能使用一次，在");
        stringBuilder.append(Constants.MAIL_EXPIRATION);
        stringBuilder.append("分钟内有效。验证成功则自动失效。<br/>");
        stringBuilder.append("如果您没有进行上述操作，请忽略此邮件。");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // 发件配置并发送邮件
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            //这里只是设置username 并没有设置host和password，因为host和password在springboot启动创建JavaMailSender实例的时候已经读取了
            mimeMessageHelper.setFrom(mailUserName);
            // 用户的邮箱地址
            mimeMessageHelper.setTo(mailAddress);
            // 邮件的标题
            mimeMessage.setSubject("邮箱验证-管理系统");
            // 上面所拼接的邮件内容
            mimeMessageHelper.setText(stringBuilder.toString(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("创建MimeMessageHelper错误{}", e.getMessage());
        }
        return "获取验证码成功，请查看移步您的邮箱" + mailAddress + "查看验证码！";
    }
}
