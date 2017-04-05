package com.muses.avancier.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muses.avancier.model.BlockUser;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.repository.BlockUserRepository;
import com.muses.avancier.repository.WxUserRepository;

/**
 * 黑名单服务
 * @author kit@muses.cc
 * @since 2017-3-29
 */
@Service
public class BlockUserService {
    
    @Autowired
    private WxUserRepository wxUserRepo;

    @Autowired
    private BlockUserRepository blockUserRepo;
    
    /**
     * 返回所有黑名单
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<BlockUser> getAllBlockUsers(Pageable pageable){
        return blockUserRepo.findAll(pageable);
    }
    
    /**
     * 将用户添加到黑名单(同时删除签到记录)
     * @param wxUserIds
     */
    @Transactional
    public void blockUser(Long[] wxUserIds){
        List<WxUser> wxUsers = wxUserRepo.findByIdIn(wxUserIds);
        List<BlockUser> blocks = new ArrayList<>();
        for(WxUser user : wxUsers){
            BlockUser block = new BlockUser();
            block.setHeadpic(user.getHeadpic());
            block.setNickname(user.getNickname());
            block.setOpenId(user.getOpenId());
            blocks.add(block);
        }
        blockUserRepo.save(blocks);
        wxUserRepo.delete(wxUsers);
    }
    
    /**
     * 删除黑名单
     * @param ids
     */
    @Transactional
    public void deleteBlockUser(Long[] ids){
        blockUserRepo.deleteByIdIn(ids);
    }
}
