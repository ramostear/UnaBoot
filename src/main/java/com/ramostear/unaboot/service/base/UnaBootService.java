package com.ramostear.unaboot.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UnaBootService<T,ID extends Serializable> {

    /**
     * 查询所有的记录
     * @return
     */
    @NonNull
    List<T> findAll();

    /**
     * 查询所有记录并对结果集排序
     * @param sort
     * @return
     */
    @NonNull
    List<T> finaAll(@NonNull Sort sort);

    /**
     * 查询所有记录，并对结果集进行分页处理
     * @param pageable
     * @return
     */
    @NonNull
    Page<T> findAll(@NonNull Pageable pageable);

    /**
     * 根据id查询所有的记录
     * @param ids
     * @return
     */
    List<T> findAllById(@NonNull Collection<ID> ids);

    /**
     * 根据id查询所有记录，并对结果集进行排序
     * @param ids
     * @param sort
     * @return
     */
    List<T> findAllById(@NonNull Collection<ID> ids,@NonNull Sort sort);

    /**
     * 判断ID对应的记录是否存在
     * @param id
     * @return
     */
    Optional<T> fetchById(@NonNull ID id);

    /**
     * 根据ID查找记录
     * @param id
     * @return
     */
    @NonNull
    T findById(@NonNull ID id);

    /**
     * 允许结果为空
     * @param id
     * @return
     */
    @Nullable
    T findByIdNullable(@NonNull ID id);

    /**
     * 结果不能为空
     * @param id
     * @return
     */
    T findByIdNonNull(@NonNull ID id);

    /**
     * 检查ID对应的记录是否存在
     * @param id
     * @return
     */
    boolean exist(@NonNull ID id);

    /**
     * 统计记录总量
     * @return
     */
    long count();

    /**
     * 创建新的记录
     * @param t
     * @return
     */
    @NonNull
    @Transactional
    T create(@NonNull T t);

    /**
     * 批量创建
     * @param collection
     * @return
     */
    @NonNull
    @Transactional
    List<T> createInBatch(@NonNull Collection<T> collection);

    /**
     * 更新记录
     * @param t
     * @return
     */
    @NonNull
    @Transactional
    T update(@NonNull T t);

    /**
     * 批量更新
     * @param collection
     * @return
     */
    @NonNull
    @Transactional
    List<T> updateInBatch(@NonNull Collection<T> collection);

    /**
     * 将所有的更新数据刷新至数据库
     */
    void flush();

    /**
     * 根据ID删除指定记录
     * @param id
     * @return
     */
    @NonNull
    @Transactional
    T delete(@NonNull ID id);

    /**
     * 删除实体
     * @param t
     * @return
     */
    @NonNull
    @Transactional
    T delete(@NonNull T t);

    /**
     * 删除多个实体
     * @param collection
     */
    @Transactional
    void delete(@NonNull Collection<T> collection);

    /**
     * 清空记录
     */
    void delete();
    /**
     * 根据指定ID删除记录
     * @param ids
     */
    @Transactional
    void deleteById(@NonNull Collection<ID> ids);

    /**
     * 允许删除为空
     * @param id
     * @return
     */
    @Nullable
    @Transactional
    T deleteNullable(@NonNull ID id);
}
