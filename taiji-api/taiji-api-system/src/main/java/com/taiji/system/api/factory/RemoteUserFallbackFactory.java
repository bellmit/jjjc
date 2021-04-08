package com.taiji.system.api.factory;

import com.taiji.common.core.web.domain.AjaxResult;
import com.taiji.system.api.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.taiji.common.core.domain.R;
import com.taiji.system.api.RemoteUserService;
import com.taiji.system.api.model.LoginUser;
import feign.hystrix.FallbackFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户服务降级处理
 * 
 * @author taiji
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public R<LoginUser> getUserInfo(String username)
            {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult feignDdd(SysUser user){
                return AjaxResult.error("新增用户失败" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> listByJob(){
                return null;
            };
        };
    }
}
