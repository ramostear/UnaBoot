package com.ramostear.unaboot.common;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 5:04.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum  ParserType {

    ARCHIVE(0),
    LATEST(1),
    POPULAR(2),
    SIMILAR(3),
    SUGGEST(4),
    TOP(5),
    TAG(6),
    CATEGORY(7),
    UNKNOWN(-1);


    private int code;

    ParserType(int code){
        this.code = code;
    }

    public static ParserType obtain(int code){
        ParserType[] types = ParserType.values();
        ParserType type = UNKNOWN;
        for (ParserType parserType : types) {
            if (parserType.code == code) {
                type = parserType;
                break;
            }
        }
        return type;
    }
}
