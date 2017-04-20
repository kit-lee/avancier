package com.muses.avancier.controller;


import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.muses.avancier.model.WxUser;
import com.muses.avancier.service.BlockUserService;
import com.muses.avancier.service.WxUserService;
import com.muses.common.util.DateUtil;

/**
 * 微信签到用户的相关管理页面
 * @author kit@muses.cc
 * @since 2017-3-28
 *
 */
@Controller
public class WxUserController {

    private static final Log log = LogFactory.getLog(WxUserController.class);
    
    @Autowired
    private WxUserService wxUserService;
    
    @Autowired
    private BlockUserService blockUserService;

    
    /**
     * 待审核微信签到信息列表页面
     * @return
     */
    @RequestMapping(value="/wxrecord", params="checked=0", method=RequestMethod.GET)
    public ModelAndView checkWxUserList(){
        return new ModelAndView("checkwxuser");
    }
    
    /**
     * 待审核微信签到信息的列表json数据（后台分页）
     * @return
     */
    @RequestMapping(value="/wxrecord/json", params="checked=0", method=RequestMethod.GET)
    @ResponseBody
    public byte[] checkWxUserListJson(@RequestParam int draw, @RequestParam int start, @RequestParam int length){
        int page = start / length;
        Pageable wrapped = new PageRequest(page, length, new Sort(Direction.DESC, "id"));
        
        Page<WxUser> wxUsers = wxUserService.listNotCheckedWxUser(wrapped);
        
        JSONObject json = new JSONObject();
        json.put("draw", draw);
        json.put("recordsTotal", wxUsers.getTotalElements());
        json.put("recordsFiltered", wxUsers.getTotalElements());
        JSONArray data = new JSONArray();
        for (int i = 0; i < wxUsers.getContent().size(); i++) {
            WxUser wxUser = wxUsers.getContent().get(i);
            String[] arr = new String[7];
            arr[0] = "";
            arr[1] = String.valueOf(wxUser.getId());
            arr[2] = wxUser.getActivity().getName();
            arr[3] = wxUser.getNickname();
            arr[4] = wxUser.getHeadpic();
            arr[5] = DateUtil.DateToString(wxUser.getCreatetime(), "yyyy-MM-dd HH:mm:ss");
            arr[6] = wxUser.getMessage();

            data.add(arr);
        }

        json.put("data", data);
        return JSON.toJSONBytes(json);
        
    }
    
    /**
     * 删除指定的微信签到信息
     * @param ids
     * @return
     */
    @RequestMapping(value="/wxrecord/{ids}", method=RequestMethod.DELETE)
    @ResponseBody
    public byte[] deleteWxUser(@PathVariable Long[] ids){
        try{
            wxUserService.delete(ids);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
    }
    
    /**
     * 将指定微信签到信息设置为审核通过
     * @param ids
     * @return
     */
    @RequestMapping(value="/wxrecord/{ids}", params="checked=1", method=RequestMethod.PUT)
    @ResponseBody
    public byte[] checkedWxUser(@PathVariable Long[] ids, @RequestParam(required = false) String tags){
        try{
            wxUserService.checkWxUser(ids, tags);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
    }
    
    /**
     * 将用户添加到黑名单
     * @param ids
     * @return
     */
    @RequestMapping(value="/wxrecord/{ids}", params="block=1", method=RequestMethod.POST)
    @ResponseBody
    public byte[] addBlockUser(@PathVariable Long[] ids){
        try{
            blockUserService.blockUser(ids);
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
        
    }
}
