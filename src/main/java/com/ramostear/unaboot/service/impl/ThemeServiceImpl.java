package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.ThemeUtils;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.repository.ThemeRepository;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ThemeServiceImpl extends UnaBootServiceImpl<Theme,Integer> implements ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository repository) {
        super(repository);
        this.themeRepository = repository;
    }

    @Override
    @Transactional
    public Theme install(HttpServletRequest request, CommonsMultipartFile file) {
        Theme theme = ThemeUtils.upload(request,file);
        if(theme == null){
            log.warn("Failed to publish theme file");
            return null;
        }else{
            themeRepository.save(theme);
            return theme;
        }
    }

    @Override
    public List<ThemeFile> loadThemeFile(String folder) {
        String fullFolderPath;
        if(StringUtils.isBlank(folder)){
            folder = "themes";
            fullFolderPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes";
        }else{
            fullFolderPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+folder;
        }
        File targetFile = new File(fullFolderPath);
        File[] subFiles = targetFile.listFiles();
        if(subFiles == null || subFiles.length <=0){
            return Collections.emptyList();
        }
        List<ThemeFile> themeFiles = new ArrayList<>(subFiles.length);
        for(File file : subFiles){
            ThemeFile themeFile = new ThemeFile();
            themeFile.setName(file.getName());
            themeFile.setPid(folder);
            themeFile.setId(folder+UnaBootConst.SEPARATOR+file.getName());
            themeFiles.add(themeFile);
        }
        return themeFiles;
    }

    @Override
    public String loadThemeFileContent(String file) {
        if(StringUtils.isBlank(file)){
            return "";
        }
        String fullFilePath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+file;
        File targetFile = new File(fullFilePath);
        if(targetFile.isDirectory()){
            return "";
        }
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuffer buffer = new StringBuffer();
        String content;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(targetFile),UnaBootConst.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            while((content = bufferedReader.readLine()) != null){
                buffer.append(content+"\n");
            }
            return buffer.toString();
        } catch (UnsupportedEncodingException e) {
            log.error("Unsupported File Encoding Exception");
            return "";
        } catch (FileNotFoundException e) {
            log.error("Target theme file not found");
            return "";
        } catch (IOException e) {
            log.error("Read theme file content error");
            return "";
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean writeContentToThemeFile(String file, String content) {
        String fullFilePath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+file;
        File targetFile = new File(fullFilePath);
        if(targetFile.isDirectory()){
            return false;
        }else{
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(targetFile),UnaBootConst.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
                writer.close();
                return true;
            } catch (UnsupportedEncodingException e) {
                log.error("Unsupported File Encoding Exception");
                return false;
            } catch (FileNotFoundException e) {
                log.error("Target theme file not found:[{}]",fullFilePath);
                return false;
            } catch (IOException e) {
                log.error("Write theme file content error");
                return false;
            }
        }

    }
}
