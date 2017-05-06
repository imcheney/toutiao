package com.nowcoder.service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * qiniu service
 * test ok
 * bug found: zone1 only, config needs to be set up at very first
 * Created by Chen on 06/05/2017.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    private static String QINIU_IMAGE_DOMAIN = "http://opign8bmk.bkt.clouddn.com/";
    Configuration cfg = new Configuration(Zone.zone1());
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "0zlRLLiXw5BD1ltGDZLi1MaOipgEBmR0X9d9wI0I";
    String SECRET_KEY = "NsXgELoqJmhSwaUXqoHZrYXmFFOduA57QawRTYPz";
    //要上传的空间
    String bucketname = "toutiao";
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager(cfg);

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos == -1) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();  //记得toLowerCase
            if (!ToutiaoUtil.checkImageFormat(fileExt)) {
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "0") + "." + fileExt;
            System.out.println(fileName);
            //调用put方法上传
            System.out.println(getUpToken());
            System.out.println(uploadManager);
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            if (res.isOK() && res.isJson()) {  //ok
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常01:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常02:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
