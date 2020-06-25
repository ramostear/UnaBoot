package com.ramostear.unaboot.web.admin;

import com.alibaba.fastjson.JSONObject;
import com.ramostear.unaboot.component.FileManager;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 13:50.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@RestController
@RequestMapping("/admin/upload")
public class FileUploadController extends UnaBootController {

    private final FileManager fileManager;

    FileUploadController(FileManager fileManager){
        this.fileManager = fileManager;
    }

    @PostMapping("/image")
    public JSONObject image(@RequestParam(name = "img") CommonsMultipartFile file){
        JSONObject json = new JSONObject();
        if(file == null || file.isEmpty()){
            return convert(json,0,"Upload file must not be null","");
        }
        if(StringUtils.isBlank(file.getOriginalFilename()) || !allowImag(file.getOriginalFilename())){
            return convert(json,0,"The format of the upload file is incorrect","");
        }
        String url = fileManager.uploadFile(file);
        if(StringUtils.isBlank(url)){
            return convert(json,0,"File upload failed","");
        }else{
            return convert(json,1,"file uploaded successfully",url);
        }
    }
    @PostMapping("/editormd")
    public JSONObject editormd(@RequestParam(name = "editormd-image-file") CommonsMultipartFile file){
        JSONObject json = new JSONObject();
        if(file == null || file.isEmpty()){
            return convert(json,0,"Upload file must not be null","");
        }
        if(StringUtils.isBlank(file.getOriginalFilename()) || !allow(file.getOriginalFilename())){
            return convert(json,0,"The format of the upload file is incorrect","");
        }
        String url = fileManager.uploadFile(file);
        if(StringUtils.isBlank(url)){
            return convert(json,0,"File upload failed","");
        }else{
            return convert(json,1,"file uploaded successfully",url);
        }
    }

    @PostMapping("/ckeditor")
    public JSONObject ckEditor(@RequestParam(name = "upload")CommonsMultipartFile file){
        if(file == null || file.isEmpty()){
            JSONObject json = new JSONObject();
            json.put("uploaded",false);
            json.put("url","");
            return json;
        }
        if(StringUtils.isBlank(file.getOriginalFilename()) || !allow(file.getOriginalFilename())){
            JSONObject json = new JSONObject();
            json.put("uploaded",false);
            json.put("url","");
            return json;
        }
        String url = fileManager.uploadFile(file);
        if(StringUtils.isBlank(url)){
            JSONObject json = new JSONObject();
            json.put("uploaded",false);
            json.put("url","");
            return json;
        }else{
            JSONObject json = new JSONObject();
            json.put("uploaded",true);
            json.put("url",url);
            return json;
        }
    }


    private JSONObject convert(JSONObject json,int status,String msg,String url){
        json.put("success",status);
        json.put("message",msg);
        json.put("url",url);
        return json;
    }

    private boolean allowImag(String fileName){
        String[] allowFiles = {".gif",".png",".jpg",".jpeg",".bpm",".svg"};
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        List<String> suffixList = Arrays.stream(allowFiles).collect(Collectors.toList());
        return suffixList.contains(suffix);
    }

    private boolean allow(String fileName){
        String[] allowFiles = {".gif",".png",".jpg",".jpeg",".bpm",".svg",".word",".xls",".ppt",".pdf",".zip",".rar"};
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        List<String> suffixList = Arrays.stream(allowFiles).collect(Collectors.toList());
        return suffixList.contains(suffix);
    }
}
