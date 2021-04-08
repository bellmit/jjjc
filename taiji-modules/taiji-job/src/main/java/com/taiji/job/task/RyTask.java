package com.taiji.job.task;

import com.alibaba.fastjson.JSONObject;
import com.taiji.common.core.domain.R;
import com.taiji.common.core.web.domain.AjaxResult;
import com.taiji.job.util.HttpUtils;
import com.taiji.system.api.RemoteUserService;
import com.taiji.system.api.domain.SysUser;
import com.taiji.system.api.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.taiji.common.core.utils.StringUtils;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 定时任务调度测试
 * 
 * @author taiji
 */
@Component("ryTask")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RyTask
{
    @Autowired
    private RemoteUserService remoteUserService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        R<List<SysUser>> result= remoteUserService.listByJob();
        List<SysUser>  userList  = result.getData();
        System.out.println(userList);
        System.out.println("执行无参方法");
    }

    /**
     * 用户数据同步
     */
    public void userAsync() throws IOException {
        try {
            //1.获取处理前xml数据
            String beforeResult = HttpUtils.httpCilentPost("http://pt.jiangxi.net/services/uumsWebService?wsdl", 1);
            //2.获取处理后xml数据
            String afterResult = HttpUtils.xmlByDom(beforeResult);
            List<SysUser> userList =  HttpUtils.getUserList(afterResult);

            for(int i = 0; i <userList.size(); i++){
                AjaxResult result = remoteUserService.feignDdd(userList.get(i));
                log.info(result.toString());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 机构数据同步
     */
    public void deptAsync() throws IOException {
        //请求地址
        String url = "http://pt.jiangxi.net/services/uumsWebService?wsdl";
        //请求报文
        String content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:test=\"http://tempuri.org/test.xsd\">\n"+
                "<soapenv:Header/>\n"+
                "<soapenv:Body>\n"+
                "<test:getAllOrgInfosForList>\n"+
                "</test:getAllOrgInfosForList>\n"+
                "</soapenv:Body>\n"+
                "</soapenv:Envelope>";
        //调用
        String result = HttpUtils.httpCilentPost(url, 1); }
}
