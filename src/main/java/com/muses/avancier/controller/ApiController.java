package com.muses.avancier.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.muses.avancier.model.Activity;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.service.ActivityService;
import com.muses.avancier.service.WxUserService;

/**
 * 前端程序调用的REST API
 * @author kit@muses.cc
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	private static final Log logger = LogFactory.getLog(ApiController.class);
	
	@Autowired
	private WxUserService service;
	
	@Autowired
	private ActivityService activityService;

	/**
	 * 添加签到用户
	 * @param openId
	 *         微信openId
	 * @param headpic
	 *         头像地址
	 * @param nickName
	 *         昵称
	 * @param message
	 *         留言
	 * @return
	 */
	@RequestMapping(value = "/activity/{activityId}/user", method=RequestMethod.POST)
	public byte[] addUser(@PathVariable Long activityId, @RequestParam String openId, 
	        @RequestParam String headpic, 
	        @RequestParam String nickName, @RequestParam String message){
		
		if(!headpic.startsWith("http://")){
			logger.info("headpic参数格式不符！"+headpic);
			return "false".getBytes();
		}
		if(openId.length()<28){
			logger.info("openId长度不符！"+openId);
			return "false".getBytes();
		}
		
		Activity activity = activityService.getActivity(activityId);
		if(activity==null){
		    return "false".getBytes();
		}
		
		WxUser user = new WxUser();
		user.setActivity(activity);
		user.setOpenId(openId);
		user.setHeadpic(headpic);
		user.setCreatetime(new Date());
		user.setTrans(false);
		user.setNickname(nickName);
		user.setMessage(message);
		if(activity.isNeedAudit())
		    user.setChecked(false);
		else
		    user.setChecked(true);
		
		try{
			boolean result = service.saveUser(user);
			return String.valueOf(result).getBytes();
		}catch(Exception ex){
			logger.error("保存用户信息时异常！", ex);
			return "false".getBytes();
		}
		
	}
	
	/**
	 * 获取未传输的签到信息（JSON格式）
	 * @return
	 */
	@RequestMapping(value = "/activity/{activityId}/user/trans", method=RequestMethod.GET)
	public byte[] getNotTransUser(@PathVariable Long activityId, @RequestParam(required=false) String clientId){
		try{
			List<WxUser> l = new ArrayList<>();
			if(clientId==null || clientId.isEmpty())
			    l = service.listNotTrans(activityId);
			else
			    l = service.listNotTransWithCache(activityId, clientId);
		
			return JSON.toJSONBytes(l, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception ex){
			logger.error("获取未传递用户数据时异常!", ex);
			return "[]".getBytes();
		}
	}
	
	/**
	 * 传输活动所有的签到消息
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/activity/{activityId}/user/all", method=RequestMethod.GET)
	public byte[] getAllTransUser(@PathVariable Long activityId){
		
		try{
			List<WxUser> l = service.listAll(activityId);
		
			return JSON.toJSONBytes(l, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception ex){
			logger.error("获取所有用户数据时异常!", ex);
			return "[]".getBytes();
		}
		
	}
	
}
