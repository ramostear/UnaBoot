package com.ramostear.unaboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 7:08.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface BaseService<T,ID extends Serializable> {

    List<T> findAll();

    List<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    List<T> findAll(Collection<ID> ids);

    List<T> findAll(Collection<ID> ids,Sort sort);

    Optional<T> getById(ID id);

    T findById(ID id);

    T findByIdNullable(ID id);

    T findByIdNonNull(ID id);

    boolean exist(ID id);

    long totalCount();

    @Transactional
    T create(T t);

    @Transactional
    List<T> create(Collection<T> sources);

    @Transactional
    T update(T t);

    @Transactional
    List<T> update(Collection<T> sources);

    void flush();

    @Transactional
    T delete(ID id);

    @Transactional
    T delete(T t);

    @Transactional
    void delete(Collection<T> sources);

    @Transactional
    void deleteByIdIn(Collection<ID> ids);

    @Transactional
    T deleteNullable(ID id);
}
