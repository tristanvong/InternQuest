package be.ehb.tristan.javaadvanced.internquest.models;

import jakarta.persistence.*;

@Entity
@Table
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String streetName;

    @Column(nullable = false)
    private int houseNumber;

    @Column(nullable = true)
    private int busNumber;

    @Column(nullable = false)
    private int postalCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;
}
