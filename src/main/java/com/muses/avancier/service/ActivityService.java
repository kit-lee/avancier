package com.muses.avancier.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.muses.avancier.model.Activity;
import com.muses.avancier.repository.ActivityRepository;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    
    public List<Activity> listActivities(Pageable pageable){
        return activityRepository.findAll(pageable).getContent();
    }
}
