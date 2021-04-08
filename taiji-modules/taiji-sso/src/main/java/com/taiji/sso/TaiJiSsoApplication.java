package com.taiji.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import com.taiji.common.security.annotation.EnableCustomConfig;
import com.taiji.common.security.annotation.EnableRyFeignClients;
import com.taiji.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author taiji
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringCloudApplication
public class TaiJiSsoApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(TaiJiSsoApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  统一登录模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
