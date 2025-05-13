package com.server.core.domain.model;

/**
 * 忘记密码对象
 *
 *
 */
public class ForgetPwdBody extends LoginBody
{
    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String verifyCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
