package com.taiji.system.api;

import com.taiji.common.core.web.domain.AjaxResult;
import com.taiji.system.api.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.taiji.common.core.constant.ServiceNameConstants;
import com.taiji.common.core.domain.R;
import com.taiji.system.api.factory.RemoteUserFallbackFactory;
import com.taiji.system.api.model.LoginUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户服务
 * 
 * @author taiji
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping(value = "/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username);

    @PostMapping(value = "/user/jobAdd")
    public AjaxResult feignDdd(@Validated @RequestBody SysUser userr);

    @GetMapping(value = "/user/listByJob")
    public R<List<SysUser>> listByJob();
}
