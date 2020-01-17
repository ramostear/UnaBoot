package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.repository.SettingRepository;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SettingServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 6:36
 * @Version since UnaBoot-1.0
 **/
@Service(value = "settingService")
public class SettingServiceImpl extends UnaBootServiceImpl<Setting,Integer> implements SettingService {
    private final SettingRepository repository;

    public SettingServiceImpl(SettingRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Map<String, Setting> convertTo() {
        List<Setting> list = findAll();
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(Setting::getKey,setting -> setting));
    }

    @Override
    public String getValue(String key) {
        Setting setting = repository.findByKey(key);
        return setting != null ? setting.getValue(): UnaBootConst.EMPTY_CHARACTER;
    }

    @Override
    @Transactional
    public void update(Collection<Setting> collection) {
        if(CollectionUtils.isEmpty(collection)){
            return;
        }
        List<Setting> list = new ArrayList<>();
        collection.forEach(item->{
            Setting setting = repository.findByKey(item.getKey());
            if(setting != null){
                setting.setValue(item.getValue());
            }else{
                setting = new Setting();
                setting.setKey(item.getKey());
                setting.setValue(item.getValue());
            }
            list.add(setting);
        });
        updateInBatch(list);
    }
}
