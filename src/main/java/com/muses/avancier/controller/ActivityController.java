package com.muses.avancier.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.WriterException;
import com.muses.avancier.model.Activity;
import com.muses.avancier.service.ActivityService;
import com.muses.avancier.type.ActivityType;
import com.muses.avancier.util.QRCodeUtil;
import com.muses.common.util.DateUtil;

/**
 * 活动管理模块的页面与操作接口
 * 
 * @author kit@muses.cc
 * @since 2017-3-28
 */
@Controller
public class ActivityController {

    private static final Log log = LogFactory.getLog(ActivityController.class);

    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private QRCodeUtil qrCodeUtil;

    /**
     * 活动管理列表页面
     * 
     * @return
     */
    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    public ModelAndView activities(HttpServletRequest request) {
        String baseUrl = request.getScheme()+"://"+request.getServerName()+
                (request.getServerPort()!=80?":"+request.getServerPort():"");
        return new ModelAndView("activityman").addObject("baseUrl", baseUrl);
    }

    /**
     * 返回活动管理页面datatable的json数据
     * 
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/activities/json", method = RequestMethod.GET)
    @ResponseBody
    public byte[] activitiesJson(@RequestParam int draw,
            @RequestParam int start, @RequestParam int length,
            HttpServletRequest request) {
        int page = start / length;
        Pageable wrapped = new PageRequest(page, length, new Sort(
                Direction.DESC, "id"));

        Page<Activity> activities = null;

        String searchValue = request.getParameter("search[value]");

        if (searchValue.isEmpty())
            activities = activityService.listActivities(wrapped);
        else
            activities = activityService.listActivities(searchValue, wrapped);

        JSONObject json = new JSONObject();
        json.put("draw", draw);
        json.put("recordsTotal", activities.getTotalElements());
        json.put("recordsFiltered", activities.getTotalElements());
        JSONArray data = new JSONArray();
        for (int i = 0; i < activities.getContent().size(); i++) {
            Activity activity = activities.getContent().get(i);
            String[] arr = new String[8];
            arr[0] = "";
            arr[1] = String.valueOf(length * page + i + 1);
            arr[2] = activity.getName();
            arr[3] = activity.getStart() != null ? DateUtil.DateToString(
                    activity.getStart(), "yyyy-MM-dd") : "";
            arr[4] = activity.getEnd() != null ? DateUtil.DateToString(
                    activity.getEnd(), "yyyy-MM-dd") : "";
            arr[5] = activity.isNeedAudit() ? "是" : "";
            arr[6] = ActivityType.barrage.name().equals(activity.getType()) ? "弹幕" : "签到";
            arr[7] = String.valueOf(activity.getId());

            data.add(arr);
        }

        json.put("data", data);
        return JSON.toJSONBytes(json);
    }

    @RequestMapping(value = "/activities", method = RequestMethod.PUT)
    @ResponseBody
    public byte[] saveOrUpdateActivity(@ModelAttribute Activity activity) {
        try {
            activityService.saveOrUpdateActivity(activity);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
    }

    @RequestMapping(value = "/activities/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public byte[] deleteActivity(@PathVariable Long[] ids) {
        try {
            activityService.deleteActivity(ids);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
    }
    
    @RequestMapping(value="/activities/{id}/qrcode", method=RequestMethod.GET)
    public void frontendLinkQrCode(@PathVariable long id, HttpServletRequest request, HttpServletResponse rep){
        rep.setHeader("Cache-Control", "no-store");
        rep.setHeader("Pragma", "no-cache");
        rep.setDateHeader("Expires", 0);
        
        String baseUrl = request.getScheme()+"://"+request.getServerName()+(request.getServerPort()!=80?":"+request.getServerPort():"");
        String frontendUrl = baseUrl+(request.getContextPath().equals("/")?"":request.getContextPath())+"/frontend?activityId="+id;
        
        try {
            qrCodeUtil.getQRCodeImage(frontendUrl, "gif", rep.getOutputStream());
        } catch (WriterException | IOException e ) {
            log.error(e.getMessage(), e);
            rep.setStatus(500);
        }
    }
}
