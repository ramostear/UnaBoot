package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.OperLog;
import com.ramostear.unaboot.repository.OperLogRepository;
import com.ramostear.unaboot.service.OperLogService;
import org.springframework.stereotype.Service;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 13:13.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("operLogService")
public class OperLogServiceImpl extends BaseServiceImpl<OperLog,Long> implements OperLogService {

    private final OperLogRepository operLogRepository;

    public OperLogServiceImpl(OperLogRepository operLogRepository) {
        super(operLogRepository);
        this.operLogRepository = operLogRepository;
    }
}
