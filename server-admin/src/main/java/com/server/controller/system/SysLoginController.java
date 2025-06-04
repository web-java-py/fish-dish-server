package com.server.controller.system;

import java.util.List;
import java.util.Set;

import com.google.code.kaptcha.Producer;
import com.server.core.domain.model.ForgetPwdBody;
import com.server.core.redis.RedisCache;
import com.server.core.text.Convert;
import com.server.service.ISysConfigService;
import com.server.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.server.constant.Constants;
import com.server.core.domain.AjaxResult;
import com.server.core.domain.entity.SysMenu;
import com.server.core.domain.entity.SysUser;
import com.server.core.domain.model.LoginBody;
import com.server.core.domain.model.LoginUser;
import com.server.utils.SecurityUtils;
import com.server.web.service.SysLoginService;
import com.server.web.service.SysPermissionService;
import com.server.web.service.TokenService;
import com.server.service.ISysMenuService;

import javax.annotation.Resource;

/**
 * 登录验证
 */
@RestController
@Api(tags = "登陆")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("登陆")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 忘记密码
     *
     * @param forgetPwdBody 忘记密码方法
     * @return 结果
     */
    @PostMapping("/forgetPwd")
    @ApiOperation("忘记密码")
    public AjaxResult forgetPwd(@RequestBody ForgetPwdBody forgetPwdBody) {
        String msg = loginService.forgetPwd(forgetPwdBody);
        return StringUtils.isEmpty(msg) ? AjaxResult.success() : AjaxResult.error(msg);
    }

    /**
     * 获取登陆功能开关
     *
     * @return
     */
    @GetMapping("/loginFunctionEnabled")
    @ApiOperation("获取登陆功能开关")
    public AjaxResult getLoginFunctionEnabled() {
        AjaxResult ajax = AjaxResult.success();
        Boolean sliderEnabled = Convert.toBool(configService.selectConfigByKey("sys.account.sliderEnabled"));
        ajax.put("sliderEnabled", sliderEnabled);
        Boolean forgetPasswordEnabled = Convert.toBool(configService.selectConfigByKey("sys.account.forgetPasswordEnabled"));
        ajax.put("forgetPasswordEnabled", forgetPasswordEnabled);
        Boolean registerUser = Convert.toBool(configService.selectConfigByKey("sys.account.registerUser"));
        ajax.put("registerUserEnabled", registerUser);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @ApiOperation("获取用户信息")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    @ApiOperation("获取路由信息")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
