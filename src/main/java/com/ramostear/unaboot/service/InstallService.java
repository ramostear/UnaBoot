package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.InstallDto;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @ClassName InstallService
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 13:41
 * @Version since UnaBoot-1.0
 **/
public interface InstallService {

    boolean install(InstallDto dto) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException;
}
