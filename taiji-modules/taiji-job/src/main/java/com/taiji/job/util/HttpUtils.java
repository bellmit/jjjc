package com.taiji.job.util;

import com.taiji.common.core.web.domain.AjaxResult;
import com.taiji.system.api.RemoteUserService;
import com.taiji.system.api.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: xiewh
 * @time: 2021/4/6 15:52
 */
@Component
@Slf4j
public class HttpUtils {

    /**
     * 使用apache的HttpClient发送http请求
     * @param url     请求URL
     * @param  flag (1-用户报文，2-机构报文)
     * @return XML String
     * @Author: WgRui
     * @Date: 2020/12/16
     */
    public static String httpCilentPost(String url, int flag) throws IOException {
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);
        String content = "";
        if( flag == 1 ){//用户报文
            content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">\n"+
                "<soapenv:Header/>\n"+
                "<soapenv:Body>\n"+
                "<test:getChineseFonts>\n"+
                "</test:getChineseFonts>\n"+
                "</soapenv:Body>\n"+
                "</soapenv:Envelope>";
        }else if(flag == 2){//机构报文
//        String content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">\n"+
//                "<soapenv:Header/>\n"+
//                "<soapenv:Body>\n"+
//                "<test:getChineseFonts>\n"+
//                "</test:getChineseFonts>\n"+
//                "</soapenv:Body>\n"+
//                "</soapenv:Envelope>";
        }
        // 将数据放入entity中
        StringEntity entity = new StringEntity(content, "UTF-8");
        httpPost.setEntity(entity);
        // 响应模型
        String result = null;
        CloseableHttpResponse response = null;
        try {
            //设置请求头
            //特别说明一下，此处为SOAP1.1协议
            //如果用的是SOAP1.2协议，改为："application/soap+xml;charset=UTF-8"
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            //命名空间+方法名
            //如为SOAP1.2协议不需要此项
            httpPost.setHeader("SOAPAction", "application/soap+xml;charset=UTF-8");
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info("响应ContentType为:" + responseEntity.getContentType());
            log.info("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
                //xml解析
                result = xmlByDom(result);
                log.info("解析后响应内容为:" + result);
            }
        } finally {
            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
        return result;

    }

    public static List<SysUser> getUserList(String xmlStr) {

        try {
            String temp = "";
            temp = xmlStr.substring(xmlStr.indexOf("<data>"),xmlStr.indexOf("</data>") + 7);
            System.out.println("截取后字符：" + temp);
            Document document = DocumentHelper.parseText(temp);
            Element rootElement = document.getRootElement();
            List<SysUser> userList = new ArrayList<SysUser>();
            // 循环根节点，获取其子节点
            for (Iterator iter = rootElement.elementIterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next(); // 获取标签对象
                SysUser tempUser = new SysUser();
                int i = 0;
                // 循环第一层data节点，获取其子节点
                for (Iterator iterInner = element.elementIterator(); iterInner.hasNext(); i++) {
                    // 获取标签对象
                    Element elementOption = (Element) iterInner.next();
                    // 获取该标签对象的名称
                    String tagName = elementOption.getName();
                    // 获取该标签对象的内容
                    String tagContent = elementOption.getTextTrim();
                    if(i == 0){
                        tempUser.setOaUserId(tagContent);
                    }
                    if(i == 1){
                        tempUser.setNickName(tagContent);
                    }
                    if(i == 2){
                        tempUser.setUserName(tagContent);
                    }
                    if(i == 4){
                        tempUser.setOaDeptCode(tagContent);
                    }
                    // 输出内容
                    log.info("标签为{},标签值为{}",tagName,tagContent);
                }
                userList.add(tempUser);
            }
            return userList;
        } catch (DocumentException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

        /**
         * @param retXml
         * @return 解析后的xml
         * @author xiewh
         */
    public static String xmlByDom(String retXml) {
        String result = "";
        try {
            Document document = DocumentHelper.parseText(retXml);
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter sw = new StringWriter();
            XMLWriter xw = new XMLWriter(sw, format);
            xw.setEscapeText(false);
            xw.write(document);
            xw.flush();
            result = sw.toString();
            log.info("最终返回报文{}", result);
        } catch (DocumentException e) {
            log.debug("DOM解析XML失败");
            e.printStackTrace();
        } catch (IOException e) {
            log.debug("IO流写入XML失败");
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws IOException, DocumentException {
        //请求地址
//        String url = "http://pt.jiangxi.net/services/uumsWebService?wsdl";
//        //请求报文
//        String content = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://WebXml.com.cn/\">\n"+
//                "<soapenv:Header/>\n"+
//                "<soapenv:Body>\n"+
//                "<test:getChineseFonts>\n"+
//                "</test:getChineseFonts>\n"+
//                "</soapenv:Body>\n"+
//                "</soapenv:Envelope>";
//        //调用
//        String result = httpCilentPost(url, content);

        String rexXml = "<soapenv:Envelope>\n" +
                "<soapenv:Body>\n" +
                "<nsl:getAllUserInfosForList>\n" +
                "<service-response>\n" +
                "<data>\n" +
                "<row>\n" +
                "<item type=\"string\">18879988575xwh</item>\n" +
                "<item type=\"string\">谢文辉</item>\n" +
                "<item type=\"string\">xiewh</item>\n" +
                "<item type=\"string\">技术保障部</item>\n" +
                "<item type=\"string\">009527</item>\n" +
                "</row>\n" +
                "<row>\n" +
                "<item type=\"string\">111111111</item>\n" +
                "<item type=\"string\">测试</item>\n" +
                "<item type=\"string\">ceshi</item>\n" +
                "<item type=\"string\">技术保障部</item>\n" +
                "<item type=\"string\">000000</item>\n" +
                "</row>\n" +
                "</data>\n" +
                "</service-response>\n" +
                "</nsl:getAllUserInfosForList>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String temp = "";

    }

}
