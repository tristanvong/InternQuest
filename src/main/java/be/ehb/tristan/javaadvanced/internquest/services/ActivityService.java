package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.exceptions.ActivityNotFoundByIdException;
import be.ehb.tristan.javaadvanced.internquest.exceptions.FormValueIncorrectException;
import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import be.ehb.tristan.javaadvanced.internquest.repositories.ActivityRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.CompanyRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                .orElseThrow(() -> new ActivityNotFoundByIdException("Activity with id " + id + " not found"));
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public void deleteActivityById(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundByIdException("Activity not found by id: " + activityId));

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

    public void validateActivityAddress(Activity activity) {
        if(activity.getActivityStartDate() == null || activity.getActivityDeadline() == null) {
            throw new FormValueIncorrectException("Start date and deadline for activity must be provided.");
        }

        if(activity.getActivityStartDate().isAfter(activity.getActivityDeadline())) {
            throw new FormValueIncorrectException("Start date cannot be before deadline.");
        }

        if(activity.getActivityStartDate().isBefore(LocalDate.now())){
            throw new FormValueIncorrectException("Start date cannot in the past.");
        }

        if(activity.getActivityDeadline().isBefore(activity.getActivityStartDate())) {
            throw new FormValueIncorrectException("Deadline cannot be before start date.");
        }
    }
}