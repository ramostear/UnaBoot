package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.valueobject.Gitalk;
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
 * @Description 系统设置
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

    @Override
    public Gitalk gitalk() {
        Gitalk gitalk = new Gitalk();
        Map<String,Setting> settings = this.convertTo();
        if(CollectionUtils.isEmpty(settings)){
            return gitalk;
        }
        String[] keys = {"gitalk_enabled","gitalk_client_id",
                         "gitalk_client_secret","gitalk_repo",
                         "gitalk_owner","gitalk_admin"};
        gitalk.setEnabled(settings.get(keys[0]) != null && (settings.get(keys[0]).getValue()).equals("1"));
        gitalk.setClientId(settings.get(keys[1])!=null?(settings.get(keys[1]).getValue()):"");
        gitalk.setClientSecret(settings.get(keys[2])!=null?(settings.get(keys[2]).getValue()):"");
        gitalk.setRepo(settings.get(keys[3])!=null?(settings.get(keys[3]).getValue()):"");
        gitalk.setOwner(settings.get(keys[4])!=null?(settings.get(keys[4]).getValue()):"");
        gitalk.setAdmin(settings.get(keys[5])!=null?(settings.get(keys[5]).getValue()):"");
        return gitalk;
    }
}
