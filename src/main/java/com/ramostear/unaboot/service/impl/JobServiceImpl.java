package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.repository.UnaBootJobRepository;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName JobServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:28
 * @Version since UnaBoot-1.0
 **/
@Service("jobService")
public class JobServiceImpl extends UnaBootServiceImpl<UnaBootJob,Integer> implements JobService {

    private final UnaBootJobRepository unaBootJobRepository;

    public JobServiceImpl(UnaBootJobRepository unaBootJobRepository) {
        super(unaBootJobRepository);
        this.unaBootJobRepository = unaBootJobRepository;
    }

    @Override
    @Transactional
    public UnaBootJob addJob(UnaBootJob job) {
        return unaBootJobRepository.save(job);
    }

    @Override
    @Transactional
    public boolean removeJob(UnaBootJob job) {
        delete(job);
        return getJob(job.getJobId()) == null;

    }

    @Override
    @Transactional
    public boolean removeJob(Integer jobId) {
        delete(jobId);
        return getJob(jobId) == null;
    }

    @Override
    public UnaBootJob getJob(Integer jobId) {
        return findById(jobId);
    }

    @Override
    public UnaBootJob findByParam(String param) {
        return unaBootJobRepository.findByParams(param);
    }

    @Override
    @Transactional
    public boolean editJob(UnaBootJob job) {
        UnaBootJob original = getJob(job.getJobId());
        if(original != null){
            BeanUtils.copyProperties(job,original,new String[]{"jobId","createTime"});
            unaBootJobRepository.save(original);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Page<UnaBootJob> findAllJobs(Pageable pageable) {
        return unaBootJobRepository.findAllByOrderByUpdateTimeDesc(pageable);
    }
}
