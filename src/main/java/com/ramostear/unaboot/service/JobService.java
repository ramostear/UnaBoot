package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.UnaBootJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {

    UnaBootJob addJob(UnaBootJob job);

    boolean removeJob(UnaBootJob job);

    boolean removeJob(Integer jobId);

    UnaBootJob getJob(Integer jobId);

    boolean editJob(UnaBootJob job);

    Page<UnaBootJob> findAllJobs(Pageable pageable);


}
