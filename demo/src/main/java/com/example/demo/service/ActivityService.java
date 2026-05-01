package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Activity;
import com.example.demo.repository.ActivityRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void log(String message){
        Activity activity = new Activity();
        activity.setMessage(message);
        activity.setCreatedAt(LocalDateTime.now());
        activityRepository.save(activity);
    }

    public List<Activity> getRecentActivities(){
        return activityRepository.findTop10ByOrderByCreatedAtDesc();
    }
}