package com.muses.avancier.service;

import java.time.Instant;
import java.util.Date;

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
    
    /**
     * 获取一个有效期内的活动
     * @param id
     * @return
     */
    @Transactional(readOnly=true)
    public Activity getActivity(Long id){
        Activity activity = activityRepository.findOne(id);
        if(activity==null)
            return null;
        
        Date now = new Date();
        long start = activity.getStart()==null ? 0 : activity.getStart().getTime();
        long end = activity.getEnd()==null ? 0 : activity.getEnd().getTime();
        
        //日期日期不满足
        if(now.getTime()<start)
            return null;
        
        if(end>0 && now.getTime()>end)
            return null;
        
        return activity;
    }
}
