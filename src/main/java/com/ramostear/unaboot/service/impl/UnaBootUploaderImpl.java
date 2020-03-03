package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.QiniuUtils;
import com.ramostear.unaboot.domain.valueobject.Qiniu;
import com.ramostear.unaboot.service.UnaBootUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @ClassName UnaBootUploaderImpl
 * @Description 文件上传服务
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 0:20
 * @Version since UnaBoot-1.0
 **/
@Deprecated
@Slf4j
@Service("unaBootUploader")
public class UnaBootUploaderImpl implements UnaBootUploader {
    @Override
    public String upload(MultipartFile file) {
        Qiniu qiniu = QiniuUtils.getConfig();
        if(qiniu != null && qiniu.isEnabled()){
            return QiniuUtils.upload(file);
        }
        return localStore(file);
    }

    @Override
    public boolean remove(String url) {
        return delete(url);
    }

    @Override
    public boolean remove(Collection<String> urls) {
        boolean flag = false;
        if(!CollectionUtils.isEmpty(urls)){
            urls.forEach(this::delete);
            flag = true;
        }
        return flag;
    }


    private String localStore(MultipartFile file){
        String storePath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"store";
        String url = "";
        String suffix = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileName = sdf.format(new Date())
                +"-"
                + UUID.randomUUID().toString().replaceAll("-","").toLowerCase()
                +suffix;
        File temp = new File(storePath+UnaBootConst.SEPARATOR+fileName);
        if(!temp.getParentFile().exists()){
            temp.getParentFile().mkdirs();
        }
        try {
            file.transferTo(temp);
            url = UnaBootConst.SEPARATOR+"store"+UnaBootConst.SEPARATOR+fileName;
        } catch (IOException e) {
            log.error("Upload to local store error:[{}]",e.getMessage());
        }
        return url;
    }

    private boolean delete(String url){
        boolean flag = false;
        if(StringUtils.isNotBlank(url)){
            if(url.startsWith("http://")||url.startsWith("https://")){
                return QiniuUtils.remove(url);
            }else{
                String targetPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+url;
                File file = new File(targetPath);
                if(file.exists() && file.isFile()){
                    file.delete();
                    flag = true;
                }
            }
        }
        return flag;
    }
}
