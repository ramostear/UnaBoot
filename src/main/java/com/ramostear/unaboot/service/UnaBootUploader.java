package com.ramostear.unaboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UnaBootUploader {

    String upload(MultipartFile file);

    boolean remove(String url);

    boolean remove(Collection<String> urls);
}
