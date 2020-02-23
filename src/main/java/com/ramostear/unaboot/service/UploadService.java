package com.ramostear.unaboot.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UploadService {

    String upload(MultipartFile file);

    boolean delete(String url);

    boolean delete(Collection<String> urls);
}
