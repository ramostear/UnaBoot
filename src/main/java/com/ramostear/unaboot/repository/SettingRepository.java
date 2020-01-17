package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.lang.NonNull;

public interface SettingRepository extends UnaBootRepository<Setting,Integer> {

    @NonNull
    Setting findByKey(@NonNull String key);
}
