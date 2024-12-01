package be.ehb.tristan.javaadvanced.internquest.models;

import jakarta.persistence.*;

@Entity
@Table
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double rarity;
}
