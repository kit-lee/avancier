package com.muses.avancier.controller;

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
import com.muses.avancier.model.BlockUser;
import com.muses.avancier.service.BlockUserService;

/**
 * 黑名单管理相关
 * @author kit@muses.cc
 * @since 2017-3-29
 *
 */
@Controller
public class BlockUserController {

    private static final Log log = LogFactory.getLog(BlockUserController.class);
    
    @Autowired
    private BlockUserService blockUserService;
    
    /**
     * 黑名单管理页面
     * @return
     */
    @RequestMapping(value="/blockusers", method=RequestMethod.GET)
    public ModelAndView blockUserManage(){
        return new ModelAndView("blockuser");
    }
    
    @RequestMapping(value="/blockusers/json", method=RequestMethod.GET)
    @ResponseBody
    public byte[] checkWxUserListJson(@RequestParam int draw, @RequestParam int start, @RequestParam int length){
        int page = start / length;
        Pageable wrapped = new PageRequest(page, length, new Sort(Direction.DESC, "id"));
        
        Page<BlockUser> blockUsers = blockUserService.getAllBlockUsers(wrapped);
        
        JSONObject json = new JSONObject();
        json.put("draw", draw);
        json.put("recordsTotal", blockUsers.getTotalElements());
        json.put("recordsFiltered", blockUsers.getTotalElements());
        JSONArray data = new JSONArray();
        for (int i = 0; i < blockUsers.getContent().size(); i++) {
            BlockUser block = blockUsers.getContent().get(i);
            String[] arr = new String[6];
            arr[0] = "";
            arr[1] = String.valueOf(block.getId());
            arr[2] = String.valueOf(length * page + i + 1);
            arr[3] = block.getNickname();
            arr[4] = block.getHeadpic();
            arr[5] = block.getOpenId();

            data.add(arr);
        }

        json.put("data", data);
        return JSON.toJSONBytes(json);
        
    }
    
    @RequestMapping(value = "/blockusers/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public byte[] deleteBlockUsers(@PathVariable Long[] ids) {
        try {
            blockUserService.deleteBlockUser(ids);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "false".getBytes();
        }
        return "true".getBytes();
    }
    
}
