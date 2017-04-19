package com.muses.avancier.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.model.WxUserResend;
import com.muses.avancier.repository.ActivityRepository;
import com.muses.avancier.repository.WxUserRepository;
import com.muses.avancier.repository.WxUserResendRepository;
import com.muses.avancier.type.ActivityType;
import com.muses.common.util.NumberUtil;

/**
 * 处理签到用户信息的服务层spring bean
 * @author kit@muses.cc
 *
 */
@Service
public class WxUserService {
	
	private static final Log logger = LogFactory.getLog(WxUserService.class);

	@Autowired
	private WxUserRepository repository;
	
	@Autowired
	private ActivityRepository activityRepo;
	
	@Autowired
    private CacheManager cacheManager;
	
	@Autowired
	private WxUserResendRepository wxUserResendRepository;

	/**
	 * 保存一个微信签到/弹幕信息
	 * @param user
	 * @return
	 */
	@Transactional
	public boolean saveUser(WxUser user){
	    String type = user.getActivity().getType();
	    if(type==null)
	        type = ActivityType.checkin.name();
	    if(ActivityType.checkin.name().equals(type)){
	        long count = repository.countByActivityAndOpenId(user.getActivity(), user.getOpenId());
	        if(count>0)
	            return false;
	    }
	        
		repository.save(user);
		
		return true;
	}
	
	/**
	 * 返回未传输的签到信息
	 * @param activityId
	 * @return
	 */
	@Transactional
	public List<WxUser> listNotTrans(Long activityId){
	    Activity activity = activityRepo.findOne(activityId); 
		List<WxUser> l = repository.findAllNotTrans(activity);
		if(l.size()>0){
			for(int i=0;i<l.size();i++){
				WxUser u = l.get(i);
				u.setTrans(true);
				l.set(i, u);
			}
			repository.save(l);
		}
		
		return l;
	}
	
	/**
	 * 返回未传输的签到信息
	 * （通过cache判断哪些未传输）
	 * @param activityId
	 * @return
	 */
	@Transactional(readOnly=true)
    public List<WxUser> listNotTransWithCache(Long activityId, String clientId){
        Activity activity = activityRepo.findOne(activityId); 
        List<WxUser> list = repository.findAllChecked(activity);
        
        Cache cache = cacheManager.getCache("trans-data");
        @SuppressWarnings("unchecked")
        Set<WxUser> transedUsers = cache.get(clientId, HashSet.class);
        if(transedUsers==null)
            transedUsers = new HashSet<>();
        List<WxUser> users = new ArrayList<>();
        for(WxUser user : list){
            if(!transedUsers.contains(user)){
                users.add(user);
                transedUsers.add(user);
            }else
                break;
        }
        cache.put(clientId, transedUsers);
        
        return users;
    }
	
	/**
	 * 返回活动所有已审的签到
	 * @param activityId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<WxUser> listAll(Long activityId){
	    Activity activity = activityRepo.findOne(activityId); 
		return repository.findAllChecked(activity);
	}
	
	/**
	 * 分页返回活动所有已审的签到
	 * @param activityId
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
    public Page<WxUser> listAll(Long activityId, Pageable pageable){
        Activity activity = activityRepo.findOne(activityId); 
        return repository.findAllChecked(activity, pageable);
    }
	
	/**
	 * 分页返回活动所有已审的签到（带关键字过滤）
	 * @param activityId
	 * @param keyword
	 *         关键字
	 * @param pageable
	 *         分页对象
	 * @return
	 */
	@Transactional(readOnly=true)
    public Page<WxUser> listAll(Long activityId, String keyword, Pageable pageable){
        Activity activity = activityRepo.findOne(activityId); 
        return repository.findAllChecked(activity, keyword, pageable);
    }
	
	/**
	 * 返回所有未审核的微信签到记录
	 * @param pageable
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<WxUser> listNotCheckedWxUser(Pageable pageable){
	    return repository.findByNotChecked(pageable);
	}
	
	/**
	 * 删除多个微信签到
	 * @param ids
	 */
	@Transactional
    public void delete(Long[] ids){
        for(Long id : ids)
            repository.delete(id);
    }
	
	/**
	 * 审核通过指定的微信签到信息
	 * @param ids
	 * @param tags
	 *         自定义标签
	 */
	@Transactional
	public void checkWxUser(Long[] ids, String tags){
	    List<WxUser> wxUsers = repository.findByIdIn(ids);
	    for(WxUser user : wxUsers){
            user.setChecked(true);
            user.setTags(tags);
	    }
	    repository.save(wxUsers);
	}
	
	/**
	 * 异步重发内容
	 * @param ids
	 * @param interval
	 * @param howManyTimes
	 */
	@Async
	public void resendWxUser(Long[] ids, int interval, int howManyTimes){
	    logger.debug("异步重发任务进入，间隔秒数="+interval+", 重发 "+howManyTimes+" 回");
	    List<WxUser> wxUsers = repository.findByIdIn(ids);
	    List<WxUserResend> resends = new ArrayList<>(); 
	    for(int i=0;i<howManyTimes;i++){
    	    for(WxUser user : wxUsers){
    	        if(ActivityType.checkin.name().equals(user.getActivity().getType()))
    	            break;
    	        if(!user.isChecked())
    	            break;
    	        
    	        int delaySeconds = NumberUtil.getRandomNumber(interval);
    	        if(delaySeconds==0)
    	            delaySeconds=1;
    	        
    	        WxUserResend resend = new WxUserResend();
    	        resend.setActivity(user.getActivity());
    	        resend.setHeadpic(user.getHeadpic());
    	        resend.setMessage(user.getMessage());
    	        resend.setNickname(user.getNickname());
    	        resend.setOpenId(user.getOpenId());
    	        resend.setResended(false);
    	        resend.setDelay(delaySeconds);
    	        
    	        resends.add(resend);
    	    }
	    }
	    logger.debug("总共"+resends.size()+"条任务");
	    
	    wxUserResendRepository.save(resends);

	}
	
	/**
	 * 清除未执行的重发任务
	 * @param activityId
	 */
	@Transactional
	public void clearScheduleTask(Long activityId){
	    Activity activity = activityRepo.findOne(activityId);
	    wxUserResendRepository.deleteByActivityAndResendedFalse(activity);
	}
	
	/**
	 * 每秒检查并执行重发任务
	 */
	@Scheduled(fixedRate=1000)
	protected void resendScheduleTask() {
	    WxUserResend resend = wxUserResendRepository.findTopByResendedFalseOrderByIdAsc();
	    if(resend==null)
	        return;

	    resend.setResended(true);
	    wxUserResendRepository.save(resend);
	    
	    if(resend.getDelay()>1)
            try {
                Thread.sleep((resend.getDelay()-1) * 1000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage(), e);
            }
	    
	    WxUser resendUser = new WxUser();
        resendUser.setActivity(resend.getActivity());
        resendUser.setChecked(true);
        resendUser.setCreatetime(new Date());
        resendUser.setHeadpic(resend.getHeadpic());
        resendUser.setMessage(resend.getMessage());
        resendUser.setNickname(resend.getNickname());
        resendUser.setOpenId(resend.getOpenId());
        resendUser.setTrans(false);
        logger.debug("重发活动["+resendUser.getActivity().getName()+"]弹幕, ["+resendUser.getNickname()+"]:"+resendUser.getMessage());
        //repository.save(resendUser);
	}
	
	/**
	 * 每天清理已重发过的任务
	 */
	@Scheduled(cron="0 0 12 * * ?")
	protected void cleanFinishResendTask(){
	    logger.info("执行计划任务，清理已执行的重发任务");
	    wxUserResendRepository.deleteByResendedTrue();
	}
}
