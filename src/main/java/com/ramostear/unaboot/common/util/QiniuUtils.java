package com.ramostear.unaboot.common.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.ramostear.unaboot.domain.valueobject.Qiniu;
import com.ramostear.unaboot.domain.valueobject.QiniuZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName QiniuUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/15 0015 23:19
 * @Version since UnaBoot-1.0
 **/
@Slf4j
public class QiniuUtils {

    private QiniuUtils(){}

    public static List<QiniuZone> zones(){
        List<QiniuZone> zones = new ArrayList<>();
        zones.add(QiniuZone.builder().name("华东").code("z0").build());
        zones.add(QiniuZone.builder().name("华北").code("z1").build());
        zones.add(QiniuZone.builder().name("华南").code("z2").build());
        zones.add(QiniuZone.builder().name("北美").code("na0").build());
        zones.add(QiniuZone.builder().name("东南亚").code("as0").build());
        return zones;
    }

    public static Qiniu getConfig(){
        Properties prop = PropertyUtils.read("cdn.properties");
        return Qiniu.builder()
                .domain(prop.getProperty("qiniu.domain"))
                .accessKey(prop.getProperty("qiniu.accessKey"))
                .secretKey(prop.getProperty("qiniu.secretKey"))
                .bucket(prop.getProperty("qiniu.bucket"))
                .zone(prop.getProperty("qiniu.zone"))
                .enabled(prop.getProperty("qiniu.enabled","false").equals("true"))
                .build();
    }

    public static Zone getQiniuZone(String code){
        Zone zone;
        switch (code){
            case "z0":
                zone = Zone.huadong();
                break;
            case "z1":
                zone = Zone.huabei();
                break;
            case "z2":
                zone= Zone.huanan();
                break;
            case "na0":
                zone = Zone.beimei();
                break;
            case "as0":
                zone= Zone.xinjiapo();
                break;
            default:
                zone = Zone.autoZone();
        }
        return zone;
    }

    public static boolean setConfig(Qiniu qiniu){
        Map<String,String> keyValue = new HashMap<>();
        keyValue.put("qiniu.domain",qiniu.getDomain());
        keyValue.put("qiniu.accessKey",qiniu.getAccessKey());
        keyValue.put("qiniu.secretKey",qiniu.getSecretKey());
        keyValue.put("qiniu.bucket",qiniu.getBucket());
        keyValue.put("qiniu.zone",qiniu.getZone());
        keyValue.put("qiniu.enabled",qiniu.isEnabled()==true?"true":"false");
        return PropertyUtils.write("cdn.properties",keyValue);
    }

    public static String upload(MultipartFile multipartFile){
        Qiniu qiniu = getConfig();
        if(qiniu != null && qiniu.isEnabled()){
            String fileName,suffix,url="";
            if(multipartFile != null && !multipartFile.isEmpty()){
                suffix = multipartFile.getOriginalFilename()
                        .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                fileName = sdf.format(new Date())
                        +"-"
                        +UUID.randomUUID().toString()
                        .replaceAll("-","").toLowerCase()
                        +suffix;
                Zone zone = getQiniuZone(qiniu.getZone());
                Configuration configuration = new Configuration(zone);
                UploadManager uploadManager = new UploadManager(configuration);
                Auth auth = Auth.create(qiniu.getAccessKey(),qiniu.getSecretKey());
                String token = auth.uploadToken(qiniu.getBucket());
                Response response;
                try {
                    response = uploadManager.put(multipartFile.getBytes(),fileName,token);
                    if(response.isOK()){
                        log.info("Upload file to qiniu is ok,info:[{}]",response.getInfo());
                        url = qiniu.getDomain()+fileName;
                    }
                } catch (QiniuException e) {
                    log.error("Upload file to qiniu error:[{}]",e.getMessage());
                } catch (IOException e) {
                   log.error("Upload file error:[{}]",e.getMessage());
                }
                return url;
            }
        }
        return "";
    }

    public static boolean remove(String url){
        Qiniu qiniu = getConfig();
        boolean flag = false;
        if(qiniu != null){
            Auth auth = Auth.create(qiniu.getAccessKey(),qiniu.getSecretKey());
            Zone zone = getQiniuZone(qiniu.getZone());
            Configuration configuration = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth,configuration);
            String key = url.replace(qiniu.getDomain(),"");
            try{
                bucketManager.delete(qiniu.getBucket(),key);
                log.info("Delete file from qiniu success,file:[{}]",key);
                flag = true;
            } catch (QiniuException e) {
                log.error("Delelte file from qiniu error:[{}]",e.getMessage());
            }
        }
        return flag;
    }

    public static boolean removeInBatch(Collection<String> urls){
        Qiniu qiniu = getConfig();
        boolean flag = false;
        if(qiniu != null){
            Auth auth = Auth.create(qiniu.getAccessKey(),qiniu.getSecretKey());
            Zone zone = getQiniuZone(qiniu.getZone());
            Configuration configuration = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth,configuration);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            urls.forEach(url->{
                String key = url.replace(qiniu.getDomain(),"");
                batchOperations.addDeleteOp(qiniu.getBucket(),key);
            });
            try {
                bucketManager.batch(batchOperations);
                flag = true;
            } catch (QiniuException e) {
                log.error("Delte multi file from qiniu error:[{}]",e.getMessage());
            }
        }
        return flag;
    }

}
