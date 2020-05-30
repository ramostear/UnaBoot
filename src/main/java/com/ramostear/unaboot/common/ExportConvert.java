package com.ramostear.unaboot.common;

import com.ramostear.unaboot.util.BeanUtils;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 4:50.
 * The following is the description information about this file:</p>
 * <p>Object export converter</p>
 */
public interface ExportConvert<X extends ExportConvert<X,Y>,Y> {

    default <T extends X> T convertFrom(Y y){
        BeanUtils.copyProperties(y,this);
        return (T)this;
    }
}
