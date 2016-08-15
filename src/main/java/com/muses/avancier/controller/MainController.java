package com.muses.avancier.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.service.WxUserService;

@RestController
@RequestMapping("/api")
public class MainController {
	
	private static final Log logger = LogFactory.getLog(MainController.class);
	
	@Autowired
	private WxUserService service;

	@RequestMapping(value = "/user", method=RequestMethod.POST)
	public byte[] addUser(@RequestParam String openId, @RequestParam String headpic){
		
		if(!headpic.startsWith("http://")){
			logger.info("headpic参数格式不符！"+headpic);
			return "false".getBytes();
		}
		if(openId.length()<28){
			logger.info("openId长度不符！"+openId);
			return "false".getBytes();
		}
		
		WxUser user = new WxUser();
		user.setOpenId(openId);
		user.setHeadpic(headpic);
		user.setCreatetime(new Date());
		user.setTrans(false);
		
		try{
			service.saveUser(user);
		}catch(Exception ex){
			logger.error("保存用户信息时异常！", ex);
			return "false".getBytes();
		}
		
		return "true".getBytes();
		
	}
	
	@RequestMapping(value = "/user/trans")
	public byte[] getNotTransUser(){
		try{
			List<WxUser> l = service.listNotTrans();
		
			return JSON.toJSONBytes(l, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception ex){
			logger.error("获取未传递用户数据时异常!", ex);
			return "[]".getBytes();
		}
	}
	
	@RequestMapping(value = "/user/all")
	public byte[] getAllTransUser(){
		
		try{
			List<WxUser> l = service.listAll();
		
			return JSON.toJSONBytes(l, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception ex){
			logger.error("获取所有用户数据时异常!", ex);
			return "[]".getBytes();
		}
		
	}
}
