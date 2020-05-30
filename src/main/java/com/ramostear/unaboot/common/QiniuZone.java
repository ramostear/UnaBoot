package com.ramostear.unaboot.common;

import com.qiniu.common.Zone;
import org.springframework.util.StringUtils;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 5:03.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum QiniuZone {

    HUADONG("z0","华东",Zone.huadong()),
    HUABEI("z1","华北",Zone.huabei()),
    HUANAN("z2","华南",Zone.huanan()),
    BEIMEI("na0","北美",Zone.beimei()),
    DONGNANYA("as0","东南亚",Zone.xinjiapo());

    private final String name;
    private final String code;
    private final Zone zone;

    QiniuZone(String code,String name,Zone zone){
        this.code = code;
        this.name = name;
        this.zone = zone;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public Zone getZone() {
        return this.zone;
    }

    public static Zone zone(String code){
        if(StringUtils.isEmpty(code)){
            return Zone.autoZone();
        }
        Zone zone = null;
        for(QiniuZone loop : QiniuZone.values()){
            if(loop.getCode().equals(code)){
                zone = loop.getZone();
            }
        }
        if(zone == null){
            zone = Zone.autoZone();
        }
        return zone;
    }
}
