package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Theme;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 18:43.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface ThemeRepository extends BaseRepository<Theme,Integer> {

    void deleteByName(String name);
}
