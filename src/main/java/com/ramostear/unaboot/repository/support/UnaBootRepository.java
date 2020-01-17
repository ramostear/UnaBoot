package com.ramostear.unaboot.repository.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;

@NoRepositoryBean
public interface UnaBootRepository<Entity,ID> extends JpaRepository<Entity,ID> {

    /**
     * 根据给定的id集合查询数据库，并对结果集进行排序
     * @param ids
     * @param sort
     * @return
     */
    @NonNull
    List<Entity> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Sort sort);

    /**
     * 根据给定的ID集合进行分页查询
     * @param ids
     * @param pageable
     * @return
     */
    @NonNull
    Page<Entity> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Pageable pageable);

    /**
     * 根据ID集合中指定的标识删除数据库中对应的数据记录
     * @param ids
     * @return
     */
    long deleteByIdIn(@NonNull Iterable<ID> ids);
}
