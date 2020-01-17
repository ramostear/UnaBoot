package com.ramostear.unaboot.repository.support;

import org.apache.shiro.util.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName UnaBootRepositoryImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 4:58
 * @Version since UnaBoot-1.0
 **/
public class UnaBootRepositoryImpl<ENTITY,ID extends Serializable> extends SimpleJpaRepository<ENTITY,ID> implements UnaBootRepository<ENTITY,ID> {

    private final EntityManager entityManager;

    private final JpaEntityInformation<ENTITY, ?> entityInformation;

    public UnaBootRepositoryImpl(JpaEntityInformation<ENTITY, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public List<ENTITY> findAllByIdIn(Iterable<ID> ids, Sort sort) {
        Assert.notNull(ids,"ids must not be null");
        Assert.notNull(sort,"sort param must not be null");
        if(!ids.iterator().hasNext()){
            return Collections.emptyList();
        }
        if(entityInformation.hasCompositeId()){
            List<ENTITY> list = new ArrayList<>();
            ids.forEach(id->super.findById(id).ifPresent(list::add));
            return list;
        }
        ByIdsSpecification<ENTITY> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<ENTITY> query = super.getQuery(specification,sort);
        return query.setParameter(specification.parameter,ids).getResultList();
    }

    @Override
    public Page<ENTITY> findAllByIdIn(Iterable<ID> ids, Pageable pageable) {
        Assert.notNull(ids,"ids must not be null");
        Assert.notNull(pageable,"pageable parameter must not be null");
        if(!ids.iterator().hasNext()){
            return new PageImpl<>(Collections.emptyList());
        }
        if(entityInformation.hasCompositeId()){
            throw new UnsupportedOperationException("Unsupported find all by composite id with page info");
        }
        ByIdsSpecification<ENTITY> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<ENTITY> query = super.getQuery(specification,pageable).setParameter(specification.parameter,ids);
        TypedQuery<Long> countQuery = getCountQuery(specification,getDomainClass()).setParameter(specification.parameter,ids);
        return pageable.isUnpaged()?new PageImpl<>(query.getResultList()):readPage(query,getDomainClass(),pageable,countQuery);
    }

    @Override
    @Transactional
    public long deleteByIdIn(Iterable<ID> ids) {
        List<ENTITY> entities = findAllById(ids);
        deleteInBatch(entities);
        return entities.size();
    }

    protected <S extends ENTITY> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, TypedQuery<Long> countQuery) {
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(),pageable,()->
                executeCountQuery(countQuery)
        );
    }

    private static long executeCountQuery(TypedQuery<Long> query) {
        org.springframework.util.Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        Long element;
        for(Iterator var4 = totals.iterator(); var4.hasNext(); total += element == null ? 0L : element) {
            element = (Long)var4.next();
        }
        return total;
    }

    private static final class ByIdsSpecification<T> implements Specification<T>{
        private static final long serialVersionUID = 991541294551341226L;
        private final JpaEntityInformation<T,?> entityInformation;
        private ParameterExpression<Iterable> parameter;
        public ByIdsSpecification(JpaEntityInformation<T,?> entityInformation){
            this.entityInformation = entityInformation;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            Path<?> path = root.get(this.entityInformation.getIdAttribute());
            this.parameter = criteriaBuilder.parameter(Iterable.class);
            return path.in(this.parameter);
        }
    }
}
