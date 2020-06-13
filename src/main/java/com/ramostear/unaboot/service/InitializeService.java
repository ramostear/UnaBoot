package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.InitializeDto;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 23:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface InitializeService {

    void configurer(InitializeDto initializeDto) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException;
}
