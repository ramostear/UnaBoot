package com.ramostear.unaboot.common.aspect.lang;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 12:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum LogType {

    OTHER(0,"其他"),      //其他
    LIST(1,"列表"),       //列表
    VIEW(2,"视图"),       //视图
    INSERT(3,"新增"),     //新增
    UPDATE(4,"修改"),     //修改
    DELETE(5,"删除"),     //删除
    GRANT(6,"授权"),      //授权
    EXPORT(7,"导出"),     //导出
    IMPORT(8,"导入"),     //导入
    FORCE(9,"强退"),      //强退
    CLEAN(10,"清空"),      //清空
    UPLOAD(11,"上传");      //上传

    private int code;
    private String text;

    private LogType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public LogType getByCode(int code){
        LogType value = LogType.OTHER;
        LogType[] types = LogType.values();
        for(LogType type : types){
            if(type.code == code){
                value = type;
                break;
            }
        }
        return value;
    }

    public String getText() {
        return this.text;
    }
}
