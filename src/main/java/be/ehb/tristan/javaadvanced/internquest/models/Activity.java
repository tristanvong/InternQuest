package be.ehb.tristan.javaadvanced.internquest.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String activityName;

    @Column(nullable = false)
    private LocalDate activityStartDate;

    @Column(nullable = false)
    private LocalDate activityDeadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public LocalDate getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(LocalDate activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public LocalDate getActivityDeadline() {
        return activityDeadline;
    }

    public void setActivityDeadline(LocalDate activityDeadline) {
        this.activityDeadline = activityDeadline;
    }
}