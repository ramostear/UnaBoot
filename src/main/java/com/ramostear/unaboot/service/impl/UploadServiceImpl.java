package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.QiniuUtils;
import com.ramostear.unaboot.domain.valueobject.Qiniu;
import com.ramostear.unaboot.service.UploadService;
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
import java.util.UUID;

/**
 * @ClassName UploadServiceImpl
 * @Description 文件上传
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 8:03
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Override
    public String upload(MultipartFile file) {
        Qiniu qiniu = QiniuUtils.getConfig();
        if(qiniu != null && qiniu.isEnabled()){
            return QiniuUtils.upload(file);
        }
        return localStore(file);
    }

    @Override
    public boolean delete(String url) {
        return deleteFile(url);
    }

    @Override
    public boolean delete(Collection<String> urls) {
        if(CollectionUtils.isEmpty(urls)){
            return false;
        }
        urls.forEach(url->deleteFile(url));
        return true;
    }

    private String localStore(MultipartFile file){
        String root = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"store";
        String path;
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileName = sdf.format(new Date())
                +"-"
                + UUID.randomUUID().toString()
                .replaceAll("-","").toLowerCase()
                +suffix;
        path = UnaBootConst.SEPARATOR+"store"+UnaBootConst.SEPARATOR+fileName;
        File temp = new File(root+UnaBootConst.SEPARATOR+fileName);
        if(!temp.getParentFile().exists()){
            temp.getParentFile().mkdirs();
        }
        try {
            file.transferTo(temp);
        }catch (IOException e){
            log.error("Upload file to local error:{}",e.getMessage());
            return "";
        }
        return path;
    }

    private boolean deleteFile(String url){
        if(StringUtils.isNotEmpty(url)){
            if(url.startsWith("http") || url.startsWith("https")){
                return QiniuUtils.remove(url);
            }else{
                String path = UnaBootConst.FILE_UPLOAD_ROOT_DIR+url;
                File file = new File(path);
                if(file.exists() && file.isFile()){
                    return file.delete();
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }
}
