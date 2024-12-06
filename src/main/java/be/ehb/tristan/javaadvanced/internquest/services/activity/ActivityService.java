package be.ehb.tristan.javaadvanced.internquest.services.activity;

import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity with id " + id + " not found"));
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }
}