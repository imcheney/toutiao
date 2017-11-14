package com.nowcoder.util;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by Chen on 05/05/2017.
 */
public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    private static final String[] ALLOWED_IMAGE_FORMATS = new String[] {"jpg", "jpeg", "png"};
    public static final String IMAGE_DIR = "/Users/Chen/Downloads/temp/toutiaoImage/";
    public static final String TOUTIAO_DOMAIN = "http://127.0.0.1:8080/";
    public static final int SYSTEM_UID = 1;
    public static final String NEWS_URL_HEAD = "http://127.0.0.1:8080/news/";
    public static final String TEST_MAIL = "imcheney@qq.com";
    public static final String REDIS_SERVER = "47.93.4.41";
    /**
     * 把运行结果的code做成json返回
     * 0: ok, 其他: 有问题
     * @param code
     * @return String类型的json串
     */
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    /**
     * json返回code额外带上msg
     * @param code
     * @param msg
     * @return
     */
    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    /**
     * 把后端的各种返回信息用一个map装好, 然后这个map转化成json返回
     * @param code
     * @param map
     * @return
     */
    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    /**
     * MD5处理, 用于密码的加密
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    public static boolean checkImageFormat(String ext) {
        for (String format:ALLOWED_IMAGE_FORMATS) {
            if (ext.toLowerCase().equals(format)) {
                return true;
            }
        }
        return false;
    }
}
