package com.ramostear.unaboot.domain.dto.support;

import com.ramostear.unaboot.common.util.UnaBootBeanUtils;
import org.springframework.lang.NonNull;

public interface ObjectOutputConverter<X extends ObjectOutputConverter<X,Y>,Y> {

    @NonNull
    default <T extends X> T convertFrom(@NonNull Y y){
        UnaBootBeanUtils.update(y,this);
        return (T)this;
    }
}
