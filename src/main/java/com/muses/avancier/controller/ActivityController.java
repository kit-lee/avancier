package com.muses.avancier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.muses.avancier.model.Activity;
import com.muses.avancier.service.ActivityService;

@Controller
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    public ModelAndView activities(Pageable pageable) {
        Pageable wrapped = new PageRequest(pageable.getPageNumber(), Math.min(
                pageable.getPageSize(), 10),
                pageable.getSort() != null ? pageable.getSort() : new Sort(
                        Direction.DESC, "id"));

        List<Activity> activities = activityService.listActivities(wrapped);

        ModelAndView view = new ModelAndView("activityman");
        view.addObject("activities", activities);
        return view;
    }
}
