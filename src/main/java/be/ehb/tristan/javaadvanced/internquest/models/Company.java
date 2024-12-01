package be.ehb.tristan.javaadvanced.internquest.models;

import be.ehb.tristan.javaadvanced.internquest.enums.Industry;
import jakarta.persistence.*;

@Entity
@Table
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nameOfCompany;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "address_id")
    private Address address;

    @Column(nullable = false)
    private Enum<Industry> industry;

    @Column(nullable = true)
    private String description;
}