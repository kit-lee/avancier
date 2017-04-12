package com.muses.avancier.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.muses.avancier.model.Activity;
import com.muses.avancier.service.ActivityService;

/**
 * 签到前端页面
 * @author kit@muses.cc
 * @since 2017-3-30
 *
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    private static final Log log = LogFactory.getLog(FrontendController.class);
    
    @Value("${app.wx_pub.appid}")
    private String wxAppId;
    
    @Autowired
    private ActivityService activityService;
    
    /**
     * 前端首页，判断是否有openid，没有则跳转到微信授权
     * @param request
     * @return
     */
    @RequestMapping
    public ModelAndView index(@RequestParam Long activityId, HttpServletRequest request){
        Activity activity = activityService.getActivity(activityId);
        String baseUrl = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()!=80?":"+request.getServerPort():"");
        ModelAndView view = new ModelAndView("/frontend/index");
        view.addObject("wxAppId", wxAppId);
        view.addObject("baseUrl", baseUrl);
        view.addObject("activity", activity);
        return view;
    }
    
    /**
     * 签到页面，根据openid获取用户信息，并且进行签到动作
     * @return
     */
    @RequestMapping("/checkin")
    public ModelAndView checkin(@RequestParam long activityId, 
            @RequestHeader ("User-Agent") String userAgent,
            HttpServletRequest request){
        Activity activity = activityService.getActivity(activityId);
        String apiAddress = request.getContextPath().equals("/")?"":request.getContextPath()+"/api/activity/"+activityId+"/user";
        ModelAndView view = new ModelAndView("/frontend/checkin");
        view.addObject("apiAddress", apiAddress);
        view.addObject("wxAppId", wxAppId);
        
        boolean isWeixin = userAgent.indexOf("micromessenger")!=-1;
        if(!isWeixin && activity!=null && 
                activity.getDefOpenId()!=null &&
                !activity.getDefOpenId().isEmpty()){
            view.addObject("openId", activity.getDefOpenId());
            view.addObject("headPic", activity.getDefHeadPic());
        }
            
        return view;
    }
    
    /**
     * 签到成功页面
     * @return
     */
    @RequestMapping("/success")
    public ModelAndView success(@RequestParam long activityId){
        ModelAndView view = new ModelAndView("/frontend/success");
        return view;
    }
    
    /**
     * 用户不批准授权的取消页面
     * @return
     */
    @RequestMapping("/cancel")
    public ModelAndView cancel(){
        ModelAndView view = new ModelAndView("/frontend/cancel");
        return view;
    }
}
