package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.vo.Gitalk;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:27.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface SettingService extends BaseService<Setting,Integer>{

    Map<String,Setting> toMap();

    @NonNull
    String getValue(@NonNull String key);

    void updateInBatch(@NonNull Collection<Setting> settings);

    Gitalk gitalk();
}
