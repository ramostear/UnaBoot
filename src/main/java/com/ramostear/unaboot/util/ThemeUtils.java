package com.ramostear.unaboot.util;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.Theme;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 19:03.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class ThemeUtils {

    private ThemeUtils(){}

    public static Theme upload(HttpServletRequest request, CommonsMultipartFile multipartFile){
        if(multipartFile.isEmpty()){
            return null;
        }
        String filename = multipartFile.getOriginalFilename();
        assert filename != null;
        int suffixIndex = filename.lastIndexOf(".");
        String suffixName = filename.substring(suffixIndex+1).toLowerCase();
        if(!suffixName.equals("zip") && ! suffixName.equals("rar")){
            return null;
        }
        String themeName = filename.substring(0,suffixIndex);
        String file = Constants.UNABOOT_STORAGE_DIR+"themes"+Constants.SEPARATOR+filename;
        String folder = Constants.UNABOOT_STORAGE_DIR+"themes"+Constants.SEPARATOR+themeName;
        File tempFile = new File(file);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        try {
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        switch (suffixName){
            case "rar" :
                try {
                    ArchiveUtils.unRar(tempFile.getAbsolutePath(),folder);
                }catch (Exception e){
                    return null;
                }
                break;
            case "zip" :
                try {
                    ArchiveUtils.unzip(tempFile,folder);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
            default:    return null;
        }
        return new Theme(themeName);
    }

    public static boolean remove(String path){
        File file = new File(path);
        return remove(file);
    }

    public static boolean remove(File file){
        if(!file.exists()){
            return false;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                Arrays.stream(files).forEach(ThemeUtils::remove);
            }
        }
        return file.delete();
    }

    public static void initializeDefaultTheme() throws IOException {
        String defaultThemePath = ThemeUtils.class.getClassLoader().getResource("theme/default").getPath();
        File sourceFile = new File(defaultThemePath);
        String destPath = Constants.UNABOOT_STORAGE_DIR+"themes"+Constants.SEPARATOR+"default";
        File destFile = new File(destPath);
        FileUtils.copyDirectory(sourceFile,destFile);
    }
}
