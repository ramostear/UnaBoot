package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:40.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface ScheduleRepository extends BaseRepository<Schedule,Integer> {

    Page<Schedule> findAllByOrderByUpdateTimeDesc(Pageable pageable);

    Schedule findByParams(String params);

    List<Schedule> findAllByState(boolean state);

    Schedule findByMethodAndCronExp(String method,String cronExp);

}
