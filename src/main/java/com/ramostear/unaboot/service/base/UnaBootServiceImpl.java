package com.ramostear.unaboot.service.base;

import com.ramostear.unaboot.common.exception.NotFoundException;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
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
 * @ClassName UnaBootServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 6:13
 * @Version since UnaBoot-1.0
 **/
public abstract class UnaBootServiceImpl<T,ID extends Serializable> implements UnaBootService<T,ID> {

    private final String beanName;

    private final UnaBootRepository<T,ID> repository;

    public UnaBootServiceImpl(UnaBootRepository repository){
        this.repository = repository;
        Class<T> clz = (Class<T>)fetchType(0);
        beanName = clz.getSimpleName();
    }

    private Type fetchType(int index){
        Assert.isTrue(index >= 0 && index <= 1,"type index must between 0 and 1");
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> finaAll(Sort sort) {
        Assert.notNull(sort,"sort must not be null");
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Assert.notNull(pageable,"page parameter must not be null");
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAllById(Collection<ID> ids) {
        return CollectionUtils.isEmpty(ids)? Collections.emptyList():repository.findAllById(ids);
    }

    @Override
    public List<T> findAllById(Collection<ID> ids, Sort sort) {
        Assert.notNull(sort,"sort must not be null");
        return CollectionUtils.isEmpty(ids)? Collections.emptyList():repository.findAllByIdIn(ids,sort);
    }

    @Override
    public Optional<T> fetchById(ID id) {
        Assert.notNull(id,beanName+" id must not be null");
        return repository.findById(id);
    }

    @Override
    public T findById(ID id) {
        return fetchById(id).orElseThrow(()-> new NotFoundException(beanName+" was not found or has been removed"));
    }

    @Override
    public T findByIdNullable(ID id) {
        return fetchById(id).orElse(null);
    }

    @Override
    public T findByIdNonNull(ID id) {
        return fetchById(id).orElse((T)new Object());
    }

    @Override
    public boolean exist(ID id) {
        Assert.notNull(id,beanName+" id must not be null");
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional
    public T create(T t) {
        return repository.save(t);
    }

    @Override
    public List<T> createInBatch(Collection<T> collection) {
        return CollectionUtils.isEmpty(collection)?Collections.emptyList():repository.saveAll(collection);
    }

    @Override
    public T update(T t) {
        return repository.saveAndFlush(t);
    }

    @Override
    public List<T> updateInBatch(Collection<T> collection) {
        return CollectionUtils.isEmpty(collection)?Collections.emptyList():repository.saveAll(collection);
    }

    @Override
    public void flush() {
        repository.flush();
    }

    @Override
    public T delete(ID id) {
        T t = findById(id);
        delete(t);
        return t;
    }

    @Override
    public T delete(T t) {
        repository.delete(t);
        return t;
    }

    @Override
    public void delete(Collection<T> collection) {
        if(!CollectionUtils.isEmpty(collection)){
            repository.deleteInBatch(collection);
        }
    }

    @Override
    public void delete() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(Collection<ID> ids) {
        if(!CollectionUtils.isEmpty(ids)){
            repository.deleteByIdIn(ids);
        }
    }

    @Override
    public T deleteNullable(ID id) {
        return fetchById(id).map(t->{
            delete(t);
            return t;
        }).orElse(null);
    }
}
