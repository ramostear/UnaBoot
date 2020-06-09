package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.vo.ThemeFolder;
import com.ramostear.unaboot.repository.ThemeRepository;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.util.ThemeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 18:44.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("themeService")
public class ThemeServiceImpl extends BaseServiceImpl<Theme,Integer> implements ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeServiceImpl(ThemeRepository themeRepository) {
        super(themeRepository);
        this.themeRepository =  themeRepository;
    }

    @Override
    @Transactional
    public Theme upload(HttpServletRequest request, CommonsMultipartFile multipartFile) {
        Theme theme = ThemeUtils.upload(request,multipartFile);
        Assert.notNull(theme,"Failed to upload theme file.");
        return themeRepository.save(theme);
    }

    @Deprecated
    @Override
    public List<ThemeFolder> loadAllFiles(String folder, boolean page) {
        if(StringUtils.isBlank(folder)){
            folder = "themes";
        }
        String fullName = Constants.UNABOOT_STORAGE_DIR+folder;
        List<ThemeFolder> list = new ArrayList<>();
        if(folder.equals("themes") && !page){
            ThemeFolder root = new ThemeFolder();
            root.setId("themes");
            root.setPid("-1");
            root.setName("主题列表");
            root.setFolder(true);
            list.add(root);
        }
        File targetFile = new File(fullName);
        if(targetFile.exists() && !targetFile.isFile()){
            File[] subFiles = targetFile.listFiles();
            if(subFiles != null && subFiles.length>0){
                for(File file : subFiles){
                    ThemeFolder subfolder = new ThemeFolder();
                    subfolder.setName(file.getName());
                    subfolder.setPid(folder);
                    subfolder.setId(folder+Constants.SEPARATOR+file.getName());
                    subfolder.setSize(UnaBootUtils.fileSize(file.length()));
                    subfolder.setModifyDate(new Date(file.lastModified()));
                    if(file.isDirectory()){
                        subfolder.setFolder(true);
                    }else{
                        subfolder.setFolder(false);
                    }
                    list.add(subfolder);
                }
            }
        }
        return list;
    }

    @Override
    public List<ThemeFolder> findAllByParent(String parent) {
        if(StringUtils.isBlank(parent)){
            parent = "themes";
        }
        String fullName = Constants.UNABOOT_STORAGE_DIR+parent;
        List<ThemeFolder> list = new ArrayList<>();
        File targetFile = new File(fullName);
        if(targetFile.exists()&& targetFile.isDirectory()){
            File[] subFiles = targetFile.listFiles();
            if(subFiles != null && subFiles.length > 0){
                String pid = parent;
                List<ThemeFolder> data = Arrays.stream(subFiles)
                       .map(item->{
                         ThemeFolder file = new ThemeFolder();
                         file.setName(item.getName());
                         file.setPid(pid);
                         file.setId(pid+Constants.SEPARATOR+item.getName());
                         file.setSize(UnaBootUtils.fileSize(item.length()));
                         file.setModifyDate(new Date(item.lastModified()));
                         return file;
                       }).collect(Collectors.toList());
                list.addAll(data);
            }
        }
        return list;
    }

    @Override
    public List<String> filter(String theme, String filter) {
        String fullPath = Constants.UNABOOT_STORAGE_DIR+"themes"+Constants.SEPARATOR+theme;
        File file = new File(fullPath);
        if(!file.exists() || file.isFile()){
            return Collections.emptyList();
        }
        File[] subFiles = file.listFiles();
        if(subFiles != null && subFiles.length > 0){
            return Arrays.stream(subFiles).filter(item->item.isFile() && item.getName().endsWith(filter))
                    .map(File::getName).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }

    }

    @Override
    public String read(String name) {
        if(StringUtils.isBlank(name)){
            return "";
        }
        String fullPath = Constants.UNABOOT_STORAGE_DIR+name;
        File file = new File(fullPath);
        if(!file.exists() || file.isDirectory()){
            return "";
        }
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        String content;
        try {
            reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(reader);
            while ((content = bufferedReader.readLine()) != null){
                builder.append(content).append("\n");
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean write(String name, String content) {
        String fullPath = Constants.UNABOOT_STORAGE_DIR+name;
        File file = new File(fullPath);
        if(!file.exists()||file.isDirectory()){
            return false;
        }else{
            try {
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(content);
                bufferedWriter.flush();
                bufferedWriter.close();
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean newFile(String fileName) {
        if(StringUtils.isBlank(fileName)){
            return false;
        }
        String fullName = Constants.UNABOOT_STORAGE_DIR+fileName;
        File file = new File(fullName);
        if(!file.exists()){
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    @Transactional
    public boolean newFolder(String folderName) {
        if(StringUtils.isBlank(folderName)){
            return false;
        }
        String[] paths = StringUtils.split(folderName,"/");
        if(paths.length==2 && paths[0].equals("themes")){
            String themeName = paths[1];
            if(!themeName.equals("default")){
                themeRepository.deleteByName(themeName);
            }
        }
        String fullName = Constants.UNABOOT_STORAGE_DIR+folderName;
        File file = new File(fullName);
        if(!file.exists()){
            return file.mkdirs();
        }else{
            return true;
        }
    }

    @Override
    public boolean remove(String fileName) {
        String fullPath = Constants.UNABOOT_STORAGE_DIR+fileName;
        return ThemeUtils.remove(fullPath);
    }

    @Override
    public boolean initialize() {
        try {
            ThemeUtils.initializeDefaultTheme();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<String> themes() {
        String themePath = Constants.UNABOOT_STORAGE_DIR+"themes";
        File file = new File(themePath);
        File[] files = file.listFiles();
        List<String> themes = new ArrayList<>();
        if(files != null && files.length > 0){
            for(File f: files){
                themes.add(f.getName());
            }
        }else{
            themes.add("default");
        }
        return themes;
    }
}
