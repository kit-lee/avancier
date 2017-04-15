package com.muses.avancier.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.Activity;

/**
 * 活动信息仓库接口
 * @author kit@muses.cc
 *
 */
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {
    /**
     * 按名称模糊查找
     * @param name
     * @param pageable
     * @return
     */
    Page<Activity> findByNameLike(String name, Pageable pageable);
    
}
