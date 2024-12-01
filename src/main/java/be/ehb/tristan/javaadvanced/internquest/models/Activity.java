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
}
