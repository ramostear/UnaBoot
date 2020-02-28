package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.valueobject.VerifyCodeVo;

import java.io.IOException;
import java.io.OutputStream;

public interface VerifyCodeGenService {

    String generate(int width, int height, OutputStream outputStream) throws IOException;

    VerifyCodeVo generate(int width,int height);
}
