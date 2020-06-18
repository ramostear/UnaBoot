package com.ramostear.unaboot.util;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.ramostear.unaboot.common.Constants;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 19:14.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class ArchiveUtils {
    private ArchiveUtils(){}

    public static void unzip(String source,String target) throws IOException {
        unzip(new File(source),target);
    }

    public static void unzip(File source,String target) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        File targetFile = new File(target);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        ZipFile zipFile = new ZipFile(source);
        for(Enumeration entries = zipFile.getEntries();entries.hasMoreElements();){
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            String entryName = zipEntry.getName();
            input = zipFile.getInputStream(zipEntry);
            String outPath = (target+"/"+entryName);
            outPath = outPath.replace("\\","/");
            File file = new File(outPath.substring(0,outPath.lastIndexOf("/")));
            if(!file.exists()){
                file.mkdirs();
            }
            if(new File(outPath).isDirectory()){
                continue;
            }
            output = new FileOutputStream(outPath);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = input.read(bytes))>0){
                output.write(bytes,0,len);
            }
        }
        zipFile.close();
        assert input != null;
        input.close();
        assert output != null;
        output.flush();
        output.close();
        System.gc();
    }

    public static void unRar(String source,String target) throws IOException, RarException {
        File targetFile = new File(target);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        Archive archive = new Archive(new File(source));
        FileHeader header = archive.nextFileHeader();
        while (header != null){
            if(header.isDirectory()){
                File file = new File(target+Constants.SEPARATOR+header.getFileNameString());
                file.mkdirs();
            }else{
                File file = new File(target+Constants.SEPARATOR+header.getFileNameString());
                try {
                    if(!file.exists()){
                        if(!file.getParentFile().exists()){
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    }
                    FileOutputStream output = new FileOutputStream(file);
                    archive.extractFile(header,output);
                    output.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            header = archive.nextFileHeader();
        }

        archive.close();
    }
}
