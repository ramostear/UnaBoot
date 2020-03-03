package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.FileSizeUtils;
import com.ramostear.unaboot.common.util.ThemeUtils;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.domain.valueobject.ThemeVo;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public List<ThemeFile> loadThemeFile(String folder,boolean isPageData) {
        String fullFolderPath;
        if(StringUtils.isBlank(folder) || folder.trim().equals("")){
            folder = "themes";
            fullFolderPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes";
        }else{
            fullFolderPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+folder;
        }
        List<ThemeFile> themeFiles = new ArrayList<>();
        if(folder.equals("themes") && !isPageData){
            ThemeFile root = new ThemeFile();
            root.setId("themes");
            root.setPid("-1");
            root.setName("主题根目录");
            root.setFolder(true);
            themeFiles.add(root);
        }
        File targetFile = new File(fullFolderPath);
        if(!targetFile.exists()|| targetFile.isFile()){
            return themeFiles;
        }
        File[] subFiles = targetFile.listFiles();
        if(subFiles != null && subFiles.length >0){
            for(File file : subFiles){
                ThemeFile themeFile = new ThemeFile();
                themeFile.setName(file.getName());
                themeFile.setPid(folder);
                themeFile.setId(folder+UnaBootConst.SEPARATOR+file.getName());
                themeFile.setSize(FileSizeUtils.autoConvert(file.length()));
                themeFile.setLastModifyDate(new Date(file.lastModified()));
                if(file.isDirectory()){
                    themeFile.setFolder(true);
                }else{
                    themeFile.setFolder(false);
                }
                themeFiles.add(themeFile);
            }
        }
        return themeFiles;
    }

    @Override
    public List<ThemeVo> loadThemes() {
        String loadPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes";
        File targetFile = new File(loadPath);
        File[] files = targetFile.listFiles();
        List<ThemeVo> themes = new ArrayList<>();
        if(files!=null && files.length > 0){
            for (File file:files){
                themes.add(ThemeVo.builder().name(file.getName()).build());
            }
        }
        return themes;
    }

    @Override
    public List<String> templateDetail(String theme) {
        String fullPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+theme;
        File file = new File(fullPath);
        if(!file.exists() || file.isFile()){
            return Collections.emptyList();
        }
        File[] subFiles = file.listFiles();
        if(subFiles != null && subFiles.length >0){
            return Arrays.stream(subFiles)
                    .filter(f -> f.isFile() && f.getName().endsWith(".html"))
                    .map(File::getName)
                    .collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public String loadThemeFileContent(String file) {
        if(StringUtils.isBlank(file)){
            return "";
        }
        String fullFilePath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+file;
        File targetFile = new File(fullFilePath);
        if(targetFile.isDirectory()){
            return "";
        }
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder buffer = new StringBuilder();
        String content;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(targetFile),UnaBootConst.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            while((content = bufferedReader.readLine()) != null){
                buffer.append(content).append("\n");
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
        String fullFilePath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+file;
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

    @Override
    public boolean newFile(String fullName) {
        if(StringUtils.isBlank(fullName)){
            return false;
        }
        String fullPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+fullName;
        File file = new File(fullPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("Create new file error:[{}]",fullPath);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean newFolder(String fullName) {
        if(StringUtils.isBlank(fullName)){
            return false;
        }
        String fullPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+fullName;
        File file = new File(fullPath);
        if(!file.exists()){
            file.mkdir();
        }
        return true;
    }

    @Override
    public boolean deleteFile(String fullName) {
        String fullPath = UnaBootConst.FILE_UPLOAD_ROOT_DIR+fullName;
        log.info("delete file from disk :[{}]",fullPath);
        return ThemeUtils.deleteFile(fullPath);
    }

    @Override
    public void initDefaultTheme() throws IOException {
            ThemeUtils.initDefaultTheme();
    }
}
