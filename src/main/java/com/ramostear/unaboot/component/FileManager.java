package com.ramostear.unaboot.component;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Collection;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 13:26.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface FileManager {

    String uploadFile(CommonsMultipartFile multipartFile);

    boolean remove(String url);

    boolean remove(Collection<String> urls);
}
