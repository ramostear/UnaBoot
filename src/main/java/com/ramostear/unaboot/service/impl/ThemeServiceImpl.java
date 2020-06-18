package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.vo.ThemeFolder;
import com.ramostear.unaboot.exception.BadRequestException;
import com.ramostear.unaboot.exception.NotFoundException;
import com.ramostear.unaboot.repository.ThemeRepository;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.ThemeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
                         String id = pid+Constants.SEPARATOR+item.getName();
                         file.setId(id.replace("\\","/"));
                         file.setFolder(item.isDirectory());
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
    public ThemeFolder loadByUrl(String url) {
        String fullPath = Constants.UNABOOT_STORAGE_DIR+url;
        fullPath = fullPath.replace("\\","/");
        File file = new File(fullPath);
        if(!file.exists() || !file.isFile()){
            throw new BadRequestException("文件不存在或文件格式不正确");
        }
        ThemeFolder themeFolder = new ThemeFolder();
        themeFolder.setPid(url.substring(0,url.lastIndexOf("/")));
        themeFolder.setId(url);
        themeFolder.setName(file.getName());
        themeFolder.setFolder(false);
        themeFolder.setSize(UnaBootUtils.fileSize(file.length()));
        themeFolder.setModifyDate(new Date(file.lastModified()));
        return themeFolder;
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
        Theme theme = null;
        String[] paths = StringUtils.split(folderName,"/");
        if(paths.length==2 && paths[0].equals("themes")){
            String themeName = paths[1];
            if(!themeName.equals("default")){
                theme = themeRepository.findByName(themeName);
                if(theme == null){
                    theme = new Theme();
                    theme.setName(themeName);
                }
            }
        }
        if(theme != null){
            theme.setUpdateTime(DateTimeUtils.now());
            themeRepository.save(theme);
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
    @Transactional
    public void remove(String pathSequence) {
        String paths[] = pathSequence.split(",");
        if(paths != null && paths.length > 0){
            for(int i=0;i<paths.length;i++){
                String fullPath = Constants.UNABOOT_STORAGE_DIR+paths[i];
                String checked = fullPath.replace("\\","/");
                if(checked.contains("themes/default")){
                    continue;
                }
                File file = new File(fullPath);
                if(!file.exists()){
                    throw new NotFoundException("当前文件不存在");
                }
                String parentPath = file.getParent();
                if(parentPath.endsWith("themes")){
                    Theme theme = themeRepository.findByName(file.getName());
                    boolean isOk = ThemeUtils.remove(fullPath);
                    if(theme != null && isOk){
                        themeRepository.delete(theme);
                    }

                }else{
                    ThemeUtils.remove(fullPath);
                }
            }
        }


    }

    @Override
    @Transactional
    public boolean rename(String url, String new_name) {
        Theme theme = null;
        String fullPath = Constants.UNABOOT_STORAGE_DIR+url;
        fullPath = fullPath.replace("\\","/");
        File oldFile = new File(fullPath);
        if(!oldFile.exists()){
            throw new NotFoundException("当前文件不存在");
        }
        String parentPath = oldFile.getParent();
        if(parentPath.endsWith("themes")){
            theme = themeRepository.findByName(oldFile.getName());
        }
        String newFullPath = parentPath+"/"+new_name;
        File newFile = new File(newFullPath);
        if(theme != null){
            theme.setName(new_name);
            return oldFile.renameTo(newFile) && themeRepository.save(theme)!= null;

        }else{
            return oldFile.renameTo(newFile);
        }
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

    @Override
    public String download(String pathSequence, HttpServletResponse response) throws IOException {
        String paths[] = pathSequence.split(",");
        List<String> pathArray = Arrays.stream(paths)
                .map(path->Constants.UNABOOT_STORAGE_DIR+path)
                .collect(Collectors.toList());
        String path;
        String name;
        if(pathArray.size() == 1){
            String filePath = pathArray.get(0);
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
            File temp = new File(filePath);
            if(temp.exists()){
                if(temp.isFile()){
                    path = filePath;
                    name = fileName;
                }else{
                    name = "UnaBootDownload_"+new Date().getTime()+".zip";
                    path = filesToZip(pathArray,name);
                }
            }else{
                return "";
            }
        }else{
            name = "UnaBootDownload_"+new Date().getTime()+".zip";
            path = filesToZip(pathArray,name);
        }
        OutputStream out;
        BufferedInputStream bi = null;
        try {
            String fileName = URLEncoder.encode(name,"UTF-8");
            bi = new BufferedInputStream(new FileInputStream(path));
            byte[] buf = new byte[1024];
            int len;
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+fileName+"\"");
            out = response.getOutputStream();
            while ((len = bi.read(buf)) > 0){
                out.write(buf,0,len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != bi){
                bi.close();
            }

        }
        return path;
    }

    private static String filesToZip(List<String> paths,String fileName){
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        String tempPath = System.getProperty("java.io.tmpdir")+fileName;
        System.out.println(tempPath);
        try {
            File zipFile = new File(tempPath);
            zipFile.deleteOnExit();
            zipFile.createNewFile();
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String path:paths){
                File file = new File(path);
                if(!file.exists()){
                    continue;
                }
                compress(file,zos,"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != zos){
                    zos.close();
                }
                if(null != fos){
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tempPath;
    }

    private static void compress(File file,ZipOutputStream zos,String parentName) throws IOException {
        if(file.exists()){
            FileInputStream fis;
            BufferedInputStream bis = null;
            byte[] buf = new byte[1024];
            try {
                if(file.isFile()){
                    ZipEntry zipEntry;
                    if(parentName.equals("")){
                      zipEntry = new ZipEntry(file.getName());
                    }else{
                        zipEntry = new ZipEntry(parentName+"/"+file.getName());
                    }

                    zos.putNextEntry(zipEntry);
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis,1024*10);
                    int len;
                    while ((len = bis.read(buf)) != -1){
                        zos.write(buf,0,len);
                    }
                    zos.closeEntry();
                    fis.close();
                }else{
                    File[] subFiles = file.listFiles();
                    if(subFiles == null || subFiles.length == 0){
                        if(parentName.equals("")){
                            zos.putNextEntry(new ZipEntry(file.getName()+"/"));
                        }else{
                            zos.putNextEntry(new ZipEntry(parentName+"/"+file.getName()+"/"));
                        }
                        zos.closeEntry();
                    }else{
                        /*zos.putNextEntry(new ZipEntry(file.getName()+"/"));
                        zos.closeEntry();*/
                        for(File f: subFiles){
                            if(parentName.equals("")){
                                compress(f,zos,file.getName());
                            }else{
                                compress(f,zos,parentName+"/"+file.getName());
                            }
                        }
                    }
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                if(null != bis){
                    bis.close();
                }
            }
        }
    }
}
