package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.ActivityRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.CompanyRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;

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

    public void deleteActivityById(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found by id: " + activityId));

        activity.getUsers().forEach(user -> {
            user.getActivities().remove(activity);
            userRepository.save(user);
        });

        activity.getCompanies().forEach(company -> {
            company.getActivities().remove(activity);
            companyRepository.save(company);
        });

        userRepository.flush();
        companyRepository.flush();
        activityRepository.flush();
        activityRepository.delete(activity);
    }
}