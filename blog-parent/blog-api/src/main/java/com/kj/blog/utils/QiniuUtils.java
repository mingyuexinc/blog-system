package com.kj.blog.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class QiniuUtils {

    public static final String url = "http://rdrafx27v.hn-bkt.clouddn.com/";

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.accessSecretKey}")
    private String accessSecretKey;

    public boolean upload(MultipartFile file,String fileName){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证，然后准备上传 (空间名称)
        String bucket = "mingyuexinc";

        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey,accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes,fileName,upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
