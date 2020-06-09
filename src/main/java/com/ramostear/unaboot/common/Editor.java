package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 14:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum Editor {
    MARKDOWN(0,"mdEditor","Markdwon编辑器"),
    CKEDITOR(1,"ckEditor","富文本编辑器");

    private int code;
    private String name;
    private String alias;

    Editor(int code, String name, String alias){
        this.code = code;
        this.name = name;
        this.alias = alias;
    }

    Editor getEditor(String name){
        Editor result = Editor.MARKDOWN;
        Editor[] types = Editor.values();
        for(Editor type : types){
            if(name.equals(type.getName())){
                result =  type;
                break;
            }
        }
        return result;
    }

    Editor getEditor(int code){
        Editor result = Editor.MARKDOWN;
        Editor[] types = Editor.values();
        for(Editor type : types){
            if(code == type.getCode()){
                result =  type;
                break;
            }
        }
        return result;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
