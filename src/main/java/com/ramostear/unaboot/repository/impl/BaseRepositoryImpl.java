package com.ramostear.unaboot.repository.impl;

import com.ramostear.unaboot.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 6:39.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class BaseRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID> {

    private final EntityManager entityManager;
    private final JpaEntityInformation<T,?> entityInformation;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public List<T> findAllByIdIn(Iterable<ID> ids, Sort sort) {
        Assert.notNull(ids,"Object id collection must not be null.");
        Assert.notNull(sort,"sort parameter must not be null.");
        if(!ids.iterator().hasNext()){
            return Collections.emptyList();
        }
        if(entityInformation.hasCompositeId()){
            List<T> list = new ArrayList<>();
            ids.forEach(id -> {super.findById(id).ifPresent(list::add);});
            return list;
        }
        IDSpecification<T> specification = new IDSpecification<>(entityInformation);
        TypedQuery<T> query = super.getQuery(specification,sort);
        return query.setParameter(specification.expression,ids).getResultList();
    }

    @Override
    public Page<T> findAllByIdIn(Iterable<ID> ids, Pageable pageable) {
        Assert.notNull("ids","id collection must not be null.");
        Assert.notNull(pageable,"pageable param must not be null.");
        if(!ids.iterator().hasNext()){
            return new PageImpl<>(Collections.emptyList());
        }
        if(entityInformation.hasCompositeId()){
            throw new UnsupportedOperationException("Unsupported find all by composite id with page info.");
        }
        IDSpecification<T> specification = new IDSpecification<>(entityInformation);
        TypedQuery<T> query = super.getQuery(specification,pageable).setParameter(specification.expression,ids);
        TypedQuery<Long> countQuery = getCountQuery(specification,getDomainClass()).setParameter(specification.expression,ids);
        return pageable.isUnpaged()?new PageImpl<>(query.getResultList()):readPage(query,getDomainClass(),pageable,countQuery);
    }

    @Override
    public long deleteByIdIn(Iterable<ID> ids) {
        List<T> entities = findAllById(ids);
        deleteInBatch(entities);
        return entities.size();
    }


    protected <S extends T> Page<S> readPage(TypedQuery<S> query,Class<S> clz,Pageable pageable,TypedQuery<Long> countQuery){
        if(pageable.isPaged()){
            query.setFirstResult((int)pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(),pageable,()->executeCountQuery(countQuery));
    }

    private static long executeCountQuery(TypedQuery<Long> query){
        Assert.notNull(query,"TypedQuery param must not be null.");
        List<Long> totalResults = query.getResultList();
        long totalCount = 0L;
        Long element;
        for(Iterator iterator = totalResults.iterator();iterator.hasNext();totalCount += element == null?0L:element){
            element = (Long) iterator.next();
        }
        return totalCount;
    }

    private static final class IDSpecification<T> implements Specification<T>{

        private static final long serialVersionUID = 6816988128774283039L;
        private final JpaEntityInformation<T,?> entityInformation;
        private ParameterExpression<Iterable> expression;
        public IDSpecification(JpaEntityInformation<T,?> entityInformation){
            this.entityInformation = entityInformation;
        }
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.expression = criteriaBuilder.parameter(Iterable.class);
            return path.in(this.expression);
        }
    }
}
