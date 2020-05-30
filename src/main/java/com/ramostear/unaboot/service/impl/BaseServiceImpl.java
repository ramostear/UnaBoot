package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.exception.NotFoundException;
import com.ramostear.unaboot.repository.BaseRepository;
import com.ramostear.unaboot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 7:19.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public abstract class BaseServiceImpl<T,ID extends Serializable> implements BaseService<T,ID> {

    private final String beanName;

    private final BaseRepository<T,ID> repository;

    public BaseServiceImpl(BaseRepository repository){
        this.repository = repository;
        Class<T> clz = (Class<T>)getType(0);
        beanName = clz.getSimpleName();
    }

    private Type getType(int index){
        Assert.isTrue(index >=0 && index <=1,"Type index must between 0 and 1");
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }


    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Collection<ID> ids) {
        return CollectionUtils.isEmpty(ids)?Collections.emptyList():repository.findAllById(ids);
    }

    @Override
    public List<T> findAll(Collection<ID> ids, Sort sort) {
        Assert.notNull(sort,"sort param must not be null.");
        return CollectionUtils.isEmpty(ids)?Collections.emptyList():repository.findAllByIdIn(ids,sort);
    }

    @Override
    public Optional<T> getById(ID id) {
        Assert.notNull(id,beanName+"`s id attribute must not be null.");
        return repository.findById(id);
    }

    @Override
    public T findById(ID id) {
        return getById(id).orElseThrow(()-> new NotFoundException(beanName+"was not found or has been removed."));
    }

    @Override
    public T findByIdNullable(ID id) {
        return getById(id).orElse(null);
    }

    @Override
    public T findByIdNonNull(ID id) {
        return getById(id).orElseThrow(()->new NotFoundException(beanName+"was not found or has been removed."));
    }

    @Override
    public boolean exist(ID id) {
        Assert.notNull(id,beanName+"`s id attribute value must not be null.");
        return repository.existsById(id);
    }

    @Override
    public long totalCount() {
        return repository.count();
    }

    @Override
    @Transactional
    public T create(T t) {
        return repository.save(t);
    }

    @Override
    @Transactional
    public List<T> create(Collection<T> sources) {
        return CollectionUtils.isEmpty(sources)?Collections.emptyList():repository.saveAll(sources);
    }

    @Override
    @Transactional
    public T update(T t) {
        Assert.notNull(t,beanName+"`s data must not be null.");
        return repository.saveAndFlush(t);
    }

    @Override
    @Transactional
    public List<T> update(Collection<T> sources) {
        return CollectionUtils.isEmpty(sources)?Collections.emptyList():repository.saveAll(sources);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    @Transactional
    public T delete(ID id) {
        Assert.notNull(id,beanName+"`s id must not be null.");
        T t = findById(id);
        delete(t);
        return t;
    }

    @Override
    @Transactional
    public T delete(T t) {
        repository.delete(t);
        return t;
    }

    @Override
    @Transactional
    public void delete(Collection<T> sources) {
        if(!CollectionUtils.isEmpty(sources)){
            repository.deleteInBatch(sources);
        }
    }

    @Override
    @Transactional
    public void deleteByIdIn(Collection<ID> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            repository.deleteByIdIn(ids);
        }
    }

    @Override
    @Transactional
    public T deleteNullable(ID id) {
        return getById(id).map(t -> {
            delete(t);
            return t;
        }).orElse(null);
    }
}
