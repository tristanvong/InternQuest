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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public void setNameOfCompany(String nameOfCompany) {
        this.nameOfCompany = nameOfCompany;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Enum<Industry> getIndustry() {
        return industry;
    }

    public void setIndustry(Enum<Industry> industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}