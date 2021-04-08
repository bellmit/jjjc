package com.taiji.job.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

/**
 * @author: xiewh
 * @time: 2021/4/6 15:57
 */
public class ConvertXmlUtil {
    /**
     * 字符串xml转换成Java对象
     * @param mapObject xml映射实体
     * @param xmlStr xml字符串
     * @return: java对象
     */
    @SuppressWarnings("unchecked")
    public static Object convertXmlStrToObject(Class mapObject, String xmlStr) {
        Object javaObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(mapObject);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            javaObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return javaObject;
    }

    /**
     * 将file类型的xml转换成Java对象
     * @param mapObject xml映射实体
     * @param xmlPath xml文件路径
     * @return java对象
     */
    @SuppressWarnings("unchecked")
    public static Object convertXmlFileToObject(Class mapObject, String xmlPath) {
        Object javaObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(mapObject);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            FileReader fr = null;
            try {
                fr = new FileReader(xmlPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            javaObject = unmarshaller.unmarshal(fr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return javaObject;
    }

}
