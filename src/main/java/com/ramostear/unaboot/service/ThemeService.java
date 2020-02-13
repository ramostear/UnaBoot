package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThemeService extends UnaBootService<Theme,Integer> {
    /**
     * Upload theme archive data
     * @param request
     * @param file
     * @return
     */
    Theme install(HttpServletRequest request, CommonsMultipartFile file);

    /**
     * Load files in the destination folder
     * @param folder
     * @return
     */
    List<ThemeFile> loadThemeFile(String folder,boolean isPageData);

    /**
     * Load theme file contents
     * @param file
     * @return
     */
    String loadThemeFileContent(String file);

    /**
     * Write new data to the theme file
     * @param file
     * @param content
     * @return
     */
    boolean writeContentToThemeFile(String file,String content);

    /**
     * Create new file
     * @param fullName
     * @return
     */
    boolean newFile(String fullName);

    /**
     * Create new folder
     * @param fullName
     * @return
     */
    boolean newFolder(String fullName);

    /**
     * Remove target file from disk.
     * @param fullName
     * @return
     */
    boolean deleteFile(String fullName);
}
