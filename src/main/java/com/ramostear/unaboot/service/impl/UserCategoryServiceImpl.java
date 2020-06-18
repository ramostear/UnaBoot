package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.UserCategory;
import com.ramostear.unaboot.repository.UserCategoryRepository;
import com.ramostear.unaboot.service.UserCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/17 0017 21:12.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("userCategoryService")
public class UserCategoryServiceImpl extends BaseServiceImpl<UserCategory,Integer> implements UserCategoryService {

    private final UserCategoryRepository userCategoryRepository;


    public UserCategoryServiceImpl(UserCategoryRepository userCategoryRepository) {
        super(userCategoryRepository);
        this.userCategoryRepository = userCategoryRepository;
    }

    @Override
    public List<Integer> findAllCategoryByUserId(Integer id) {
        return userCategoryRepository.findAllCategoryIdByUserId(id);
    }

    @Override
    @Transactional
    public List<UserCategory> mergeOrCreateIfAbsent(Integer userId, List<Integer> categoryIds) {
        if(categoryIds == null || categoryIds.size() ==0){
            List<UserCategory> userCategories = userCategoryRepository.findAllByUserId(userId);
            if(!CollectionUtils.isEmpty(userCategories)){
                userCategoryRepository.deleteInBatch(userCategories);
            }
        }
        assert categoryIds != null;
        List<UserCategory> ucs = categoryIds.stream()
                .map(categoryId->new UserCategory(userId,categoryId))
                .collect(Collectors.toList());
        List<UserCategory> removeItems = new LinkedList<>(),saveItems = new LinkedList<>();
        List<UserCategory> originalItems = userCategoryRepository.findAllByUserId(userId);
        originalItems.forEach(item->{
            if(!ucs.contains(item)){
                removeItems.add(item);
            }
        });
        ucs.forEach(item->{
            if(!originalItems.contains(item)){
                saveItems.add(item);
            }
        });
        super.delete(removeItems);
        originalItems.removeAll(removeItems);
        originalItems.addAll(super.create(saveItems));
        return originalItems;
    }

    @Override
    public List<UserCategory> findAllByUserId(Integer userId) {
        return userCategoryRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public void removeBy(Integer userId, Set<Integer> categoryIds) {
        List<UserCategory> ucs = userCategoryRepository.findAllByUserIdAndCategoryIdIn(userId,categoryIds);
        if(!CollectionUtils.isEmpty(ucs)){
            userCategoryRepository.deleteInBatch(ucs);
        }
    }

    @Override
    @Transactional
    public void removeBy(Integer userId) {
        userCategoryRepository.deleteAllByUserId(userId);
    }
}
