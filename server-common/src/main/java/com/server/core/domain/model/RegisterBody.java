package com.server.core.domain.model;

/**
 * 用户注册对象
 * 
 *
 */
public class RegisterBody extends LoginBody
{
    /**
     * 再次密码
     */
    private String confirmPassword;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮箱验证码
     */
    private String verifyCode;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

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
