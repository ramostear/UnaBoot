package com.ramostear.unaboot.common.util;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.ramostear.unaboot.common.UnaBootConst;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * 压缩包工具类
 */
public class ArchiveUtils {

    public static void unzip(String source,String target) throws IOException {
        unzip(new File(source),target);
    }

    public static void unzip(File source,String target) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File targetFile = new File(target);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        ZipFile zipFile = new ZipFile(source);
        for(Enumeration entries = zipFile.getEntries();entries.hasMoreElements();){
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String entryName = zipEntry.getName();
            inputStream = zipFile.getInputStream(zipEntry);
            String outPath = (target+"/"+entryName).replaceAll("\\*","/");
            File file = new File(outPath.substring(0,outPath.lastIndexOf("/")));
            if(!file.exists()){
                file.mkdirs();
            }
            if(new File(outPath).isDirectory()){
                continue;
            }
            outputStream = new FileOutputStream(outPath);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes))>0){
                outputStream.write(bytes,0,length);
            }
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        zipFile.close();
        System.gc();
    }

    public static void unRar(String source,String target) throws Exception{
        File targetFile = new File(target);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        Archive archive = new Archive(new File(source));
        if(archive != null){
            FileHeader header = archive.nextFileHeader();
            while(header != null){
                if(header.isDirectory()){
                    File file = new File(target+ UnaBootConst.SEPARATOR+header.getFileNameString());
                    file.mkdirs();
                }else{
                    File file = new File(target+UnaBootConst.SEPARATOR+header.getFileNameString());
                    try{
                        if(!file.exists()){
                            if(!file.getParentFile().exists()){
                                file.getParentFile().mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream outputStream = new FileOutputStream(file);
                        archive.extractFile(header,outputStream);
                        outputStream.close();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                header = archive.nextFileHeader();
            }
        }
        archive.close();
    }
}
