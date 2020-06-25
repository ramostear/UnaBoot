package com.ramostear.unaboot.component.impl;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.component.FileManager;
import com.ramostear.unaboot.domain.vo.QiniuProperty;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.QiniuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 13:28.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component(value = "fileManager")
public class FileManagerImpl implements FileManager {
    @Override
    public String uploadFile(CommonsMultipartFile multipartFile) {
        QiniuProperty property = QiniuUtils.getProperties();
        if(property != null && property.isEnabled()){
            return QiniuUtils.upload(multipartFile);
        }else{
            return localStorage(multipartFile);
        }
    }

    @Override
    public boolean remove(String url) {
        return deleteFile(url);
    }

    @Override
    public boolean remove(Collection<String> urls) {
        if(CollectionUtils.isEmpty(urls)){
            return false;
        }else{
            return urls.stream().allMatch(this::deleteFile);
        }
    }

    private String localStorage(CommonsMultipartFile multipartFile){
        String root = Constants.UNABOOT_STORAGE_DIR+"storage";
        String path;
        String suffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fileName = sdf.format(DateTimeUtils.now())
                +"-"
                + UUID.randomUUID().toString().replaceAll("-","").toLowerCase()
                +suffix;
        File file = new File(root+Constants.SEPARATOR+fileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(file);
            path = "/"+fileName;
        } catch (IOException e) {
            e.printStackTrace();
            path = "";
        }
        return path;
    }

    private boolean deleteFile(String url){
        if(StringUtils.isNotBlank(url)){
            if(url.startsWith("http") || url.startsWith("https")){
                return QiniuUtils.delete(url);
            }else{
                String path = Constants.UNABOOT_STORAGE_DIR+"storage"+url;
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
