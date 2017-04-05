package com.muses.avancier.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.muses.avancier.model.BlockUser;


public interface BlockUserRepository extends PagingAndSortingRepository<BlockUser, Long> {

    /**
     * 批量删除黑名单
     * @param ids
     */
    void deleteByIdIn(Long[] ids);
}
