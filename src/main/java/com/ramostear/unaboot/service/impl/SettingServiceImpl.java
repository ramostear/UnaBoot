package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.GitalkKeys;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.vo.Gitalk;
import com.ramostear.unaboot.repository.SettingRepository;
import com.ramostear.unaboot.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:31.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("settingService")
public class SettingServiceImpl extends BaseServiceImpl<Setting,Integer> implements SettingService {

    private final SettingRepository settingRepository;

    @Autowired
    public SettingServiceImpl(SettingRepository settingRepository) {
        super(settingRepository);
        this.settingRepository = settingRepository;
    }

    @Override
    public Map<String, Setting> toMap() {
        List<Setting> list = settingRepository.findAll();
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(Setting::getKey,setting->setting));
    }

    @Override
    public String getValue(String key) {
        Setting setting = settingRepository.findByKey(key);
        return setting != null ? setting.getValue():"";
    }

    @Override
    @Transactional
    public void updateInBatch(Collection<Setting> settings) {
        if(!CollectionUtils.isEmpty(settings)){
            List<Setting> list = new ArrayList<>();
            settings.forEach(item -> {
                Setting setting = settingRepository.findByKey(item.getKey());
                if(setting != null){
                    setting.setValue(item.getValue());
                }else{
                    setting = new Setting();
                    setting.setKey(item.getKey());
                    setting.setValue(item.getValue());
                }
                list.add(setting);
            });
            update(list);
        }

    }

    @Override
    public Gitalk gitalk() {
        Gitalk gitalk = new Gitalk();
        Map<String,Setting> data = this.toMap();
        if(!CollectionUtils.isEmpty(data)){
            gitalk.setEnabled(data.get(GitalkKeys.ENABLED.getKey()) != null && (data.get(GitalkKeys.ENABLED.getKey()).getValue()).equals("1") );
            gitalk.setClientId(data.get(GitalkKeys.CLIENT_ID.getKey()) != null? data.get(GitalkKeys.CLIENT_ID.getKey()).getValue():"");
            gitalk.setClientSecret(data.get(GitalkKeys.CLIENT_SECRET.getKey()) != null? data.get(GitalkKeys.CLIENT_SECRET.getKey()).getValue():"");
            gitalk.setRepo(data.get(GitalkKeys.REPO.getKey()) != null? data.get(GitalkKeys.REPO.getKey()).getValue():"");
            gitalk.setOwner(data.get(GitalkKeys.OWNER.getKey()) != null? data.get(GitalkKeys.OWNER.getKey()).getValue():"");
            gitalk.setAdmin(data.get(GitalkKeys.ADMIN.getKey()) != null? data.get(GitalkKeys.ADMIN.getKey()).getValue():"");
        }
        return gitalk;
    }
}
