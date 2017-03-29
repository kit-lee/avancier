package com.muses.avancier.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muses.avancier.model.Activity;
import com.muses.avancier.repository.ActivityRepository;

@Service
public class ActivityService {
    
    private static final Log log = LogFactory.getLog(ActivityService.class);

    @Autowired
    private ActivityRepository activityRepository;
    
    /**
     * 分页返回活动记录
     * @param pageable
     * @return
     */
    @Transactional(readOnly=true)
    public Page<Activity> listActivities(Pageable pageable){
        return activityRepository.findAll(pageable);
    }
    
    @Transactional(readOnly=true)
    public Page<Activity> listActivities(String name, Pageable pageable){
        name = "%"+name+"%";
        return activityRepository.findByNameLike(name, pageable);
    }
    
    /**
     * 返回总活动记录数
     * @return
     
    public long countActivities(){
        return activityRepository.count();
    }*/
    
    /**
     * 保存更新活动信息
     * @param activity
     */
    @Transactional
    public void saveOrUpdateActivity(Activity activity){
        activityRepository.save(activity);
    }
    
    /**
     * 删除活动
     * @param id
     */
    @Transactional
    public void deleteActivity(Long[] ids){
        for(Long id : ids)
            activityRepository.delete(id);
    }
}
