package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.vo.ThemeFolder;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 18:44.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface ThemeService extends BaseService<Theme,Integer> {

    Theme upload(HttpServletRequest request, CommonsMultipartFile multipartFile);

    List<ThemeFolder> loadAllFiles(String folder,boolean page);

    List<ThemeFolder> findAllByParent(String parent);

    List<String> filter(String theme,String filter);

    ThemeFolder loadByUrl(String url);

    String read(String file);

    boolean write(String file,String content);

    boolean newFile(String fileName);

    boolean newFolder(String folderName);

    void remove(String fileName);

    boolean rename(String url,String new_name);

    boolean initialize();

    List<String> themes();

    String download(String pathSequence, HttpServletResponse response) throws IOException;
}
