package com.ramostear.unaboot.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.ramostear.unaboot.common.QiniuZone;
import com.ramostear.unaboot.domain.vo.QiniuProperty;
import com.ramostear.unaboot.domain.vo.QiniuZoneNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 5:12.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class QiniuUtils {

    private QiniuUtils(){}

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    /**
     *  Wrap the name and code of Qiniu Cloud storage zone as a object collection.
     * @return  object collection
     */
    public static List<QiniuZoneNode> zoneNodes(){
        List<QiniuZoneNode> elements = new ArrayList<>();
        QiniuZone[] zones = QiniuZone.values();
        Arrays.stream(zones).forEach(zone-> elements.add(new QiniuZoneNode(zone.getName(),zone.getCode())));
        return elements;
    }

    /**
     * Get the Configuration properties of Qiniu Cloud
     * @return  QiniuProperty Object
     */
    public static QiniuProperty getProperties(){
        Properties prop = UnaBootUtils.loadPropertyFile("cdn.properties");
        assert prop != null;
        return QiniuProperty.builder()
                .domain(prop.getProperty("qiniu.domain",""))
                .accessKey(prop.getProperty("qiniu.accessKey",""))
                .secretKey(prop.getProperty("qiniu.secretKey",""))
                .bucket(prop.getProperty("qiniu.bucket",""))
                .zone(prop.getProperty("qiniu.zone",QiniuZone.HUADONG.getCode()))
                .enabled(prop.getProperty("qiniu.enabled","false").equals("true"))
                .build();
    }

    /**
     * Get qiniu cloud zone according to code.
     * @param code      zone code
     * @return          zone
     */
    public static Zone getZoneByCode(String code){
        return QiniuZone.zone(code);
    }

    /**
     * Set the Configuration properties of Qiniu Cloud
     * @param property      QiniuProperty Object
     * @return              boolean
     */
    public static boolean setProperties(QiniuProperty property){
        Map<String,String> kv = new HashMap<>();
        kv.put("qiniu.domain",property.getDomain());
        kv.put("qiniu.accessKey",property.getAccessKey());
        kv.put("qiniu.secretKey",property.getSecretKey());
        kv.put("qiniu.bucket",property.getBucket());
        kv.put("qiniu.zone",property.getZone());
        kv.put("qiniu.enabled",property.isEnabled()?"true":"false");
        return UnaBootUtils.setPropertyFile("cdn.properties",kv);
    }

    /**
     * Upload file to qiniu cloud.
     * @param file          file
     * @return              file url
     */
    public static String upload(MultipartFile file){
        QiniuProperty property = getProperties();
        String fileName,suffix,url="";
        if(property != null && property.isEnabled()){
            if(file != null && !file.isEmpty()){
                suffix = Objects.requireNonNull(file.getOriginalFilename())
                        .substring(file.getOriginalFilename().lastIndexOf("."));
                fileName = SIMPLE_DATE_FORMAT.format(DateTimeUtils.now())
                        +"-"+UUID.randomUUID().toString().replaceAll("-","").toLowerCase()
                        +suffix;
                Zone zone = getZoneByCode(property.getZone());
                Configuration configuration = new Configuration(zone);
                UploadManager uploader = new UploadManager(configuration);
                Auth auth = Auth.create(property.getAccessKey(),property.getSecretKey());
                String token = auth.uploadToken(property.getBucket());
                Response response;
                try {
                    response = uploader.put(file.getBytes(),fileName,token);
                    if(response.isOK()){
                        if(property.getDomain().endsWith("/")){
                            url = property.getDomain()+fileName;
                        }else{
                            url = property.getDomain()+"/"+fileName;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return url;
    }

    /**
     * Delete file from qiniu cloud according to url.
     * @param url       file url
     * @return          true/false
     */
    public static boolean delete(String url){
        QiniuProperty property = getProperties();
        boolean flag = false;
        if(property != null){
            Auth auth = Auth.create(property.getAccessKey(),property.getSecretKey());
            Zone zone = getZoneByCode(property.getZone());
            Configuration configuration = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth,configuration);
            String key = property.getDomain().endsWith("/")?
                    url.replace(property.getDomain(),""):
                    url.replace(property.getDomain()+"/","");
            try {
                bucketManager.delete(property.getBucket(),key);
                flag = true;
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * delete file in batch from qiniu cloud.
     * @param urls          file url collection
     * @return              true/false
     */
    public static boolean deleteInBatch(Collection<String> urls){
        QiniuProperty property = getProperties();
        boolean flag = false;
        if(property != null){
            Auth auth = Auth.create(property.getAccessKey(),property.getSecretKey());
            Zone zone = getZoneByCode(property.getZone());
            Configuration configuration = new Configuration(zone);
            BucketManager bucketManager = new BucketManager(auth,configuration);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            urls.forEach(url->{
                String key = property.getDomain().endsWith("/")?
                        url.replace(property.getDomain(),""):
                        url.replace(property.getDomain()+"/","");
                batchOperations.addDeleteOp(property.getBucket(),key);
            });
            try {
                bucketManager.batch(batchOperations);
                flag = true;
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static List<FileInfo> findAll(){
        QiniuProperty property = getProperties();
        List<FileInfo> list = new ArrayList<>();
        if(property != null){
            Zone zone = getZoneByCode(property.getZone());
            Configuration configuration = new Configuration(zone);
            Auth auth = Auth.create(property.getAccessKey(),property.getSecretKey());
            BucketManager bucketManager = new BucketManager(auth,configuration);
            int limit = 1000;
            String prefix = "";
            String delimiter = "";
            BucketManager.FileListIterator iterator = bucketManager.createFileListIterator(property.getBucket(),prefix,limit,delimiter);
            while (iterator.hasNext()){
                FileInfo[] items = iterator.next();
                list.addAll(Arrays.asList(items));
            }
        }
        return list;
    }

    public static Page<FileInfo> findAll(Pageable pageable){
        List<FileInfo> list = findAll();
        int start = (int)pageable.getOffset();
        int end  = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start,end),pageable,list.size());
    }
}
