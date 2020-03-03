package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnaBootJobRepository extends UnaBootRepository<UnaBootJob,Integer> {

    Page<UnaBootJob> findAllByOrderByUpdateTimeDesc(Pageable pageable);

    UnaBootJob findByParams(String params);

    List<UnaBootJob> findAllByJobState(boolean jobState);
}
