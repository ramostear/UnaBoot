package com.ramostear.unaboot.common.util;

public class FileSizeUtils {

    private FileSizeUtils(){}

    public static String autoConvert(long size){
        if(size < 1024){
            return size+"Bytes";
        }else if (size >= 1024 && size < 1024*1024){
            return (int)size/1024+"KB";
        }else if(size >= 1024* 1024 && size < 1024*1024*1024){
            return (int)size/(1024*1024)+"MB";
        }else{
            return size/(1024*1024*1024)+"TB";
        }
    }
}
