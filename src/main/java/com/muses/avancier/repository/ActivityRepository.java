package com.muses.avancier.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.Activity;

public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long> {

    /**
     * 列表所有活动
     */
    Page<Activity> findAll(Pageable pageable);
}
