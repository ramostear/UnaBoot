package com.ramostear.unaboot.web.admin;

import com.alibaba.fastjson.JSONObject;
import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.service.UploadService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName UnloadController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 8:00
 * @Version since UnaBoot-1.0
 **/
@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
@RestController
@RequestMapping("/admin/upload")
public class UnloadController extends UnaBootController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/editormd")
    public JSONObject upload(@RequestParam(name="editormd-image-file")MultipartFile file){
        JSONObject json = new JSONObject();
        if(file == null || file.isEmpty()){
            return convert(json,0,"上传文件不能为空","");
        }
        if(StringUtils.isBlank(file.getOriginalFilename()) || !allow(file.getOriginalFilename())){
            return convert(json,0,"上传文件格式不正确","");
        }
        String url = uploadService.upload(file);
        if(StringUtils.isBlank(url)){
            return convert(json,0,"文件上传失败","");
        }else{
            return convert(json,1,"文件上传成功",url);
        }
    }

    private JSONObject convert(JSONObject json,int status,String message,String url){
        json.put("success",status);
        json.put("message",message);
        json.put("url",url);
        return json;
    }

    private boolean allow(String fileName){
        String[] allowFiles = {".gif",".png",".jpg",".jpeg",".bpm"};
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        List<String> suffixList = Arrays.stream(allowFiles).collect(Collectors.toList());
        return suffixList.contains(suffix);
    }
}
