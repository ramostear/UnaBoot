package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Setting;
import org.springframework.lang.NonNull;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:26.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface SettingRepository extends BaseRepository<Setting,Integer> {

    @NonNull
    Setting findByKey(@NonNull String key);
}
