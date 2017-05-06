package com.nowcoder;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;

/**
 * Created by Chen on 06/05/2017.
 */
public class QiniuTest {
    public static void main(String[] args) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "0zlRLLiXw5BD1ltGDZLi1MaOipgEBmR0X9d9wI0I";
        String secretKey = "NsXgELoqJmhSwaUXqoHZrYXmFFOduA57QawRTYPz";
        String bucket = "toutiao";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
    }
}
