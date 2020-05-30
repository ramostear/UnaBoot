package com.ramostear.unaboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 6:30.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T,ID> {

    /**
     * Query the database based on the given id set and sort the result set.
     * @param ids       given id set
     * @param sort      sort
     * @return          target data
     */
    @NonNull
    List<T> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Sort sort);

    /**
     * Pagination query according to the given id set.
     * @param ids           given id set
     * @param pageable      pagination data
     * @return              result data.
     */
    Page<T> findAllByIdIn(@NonNull Iterable<ID> ids, @NonNull Pageable pageable);

    /**
     * Delete the data records in the database according to the identifiers in the collection.
     * @param ids   given id set
     * @return      records
     */
    long deleteByIdIn(@NonNull Iterable<ID> ids);
}
