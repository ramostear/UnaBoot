package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.valueobject.Gitalk;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Map;

public interface SettingService extends UnaBootService<Setting,Integer> {

    /**
     * 将数据库记录转换为Map
     * @return
     */
    Map<String,Setting> convertTo();

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    @NonNull
    String getValue(@NonNull String key);

    /**
     * 更新
     * @param collection
     */
    void update(@NonNull Collection<Setting> collection);


    Gitalk gitalk();


}
