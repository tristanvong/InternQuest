package be.ehb.tristan.javaadvanced.internquest.models;

import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 12,message = "Username needs to be between 5 and 12 characters long.")
    private String username;

    @Column(nullable = false)
    //@Size(min = 8, max = 32, message = "Password needs to be between 8 and 32 characters long.")
    private String password;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "First name needs to be between 2 and 20 characters.")
    @JsonProperty("first_name")
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Last name needs to be between 2 and 20 characters.")
    @JsonProperty("last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String profilePicturePath;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_achievements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private Set<Achievement> achievements;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_activities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private Set<Activity> activities;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_companies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies;

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Set<Achievement> achievements) {
        this.achievements = achievements;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(min = 5, max = 12, message = "Username needs to be between 5 and 12 characters long.") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 5, max = 12, message = "Username needs to be between 5 and 12 characters long.") String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @Size(min = 2, max = 20, message = "First name needs to be between 2 and 20 characters.") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 2, max = 20, message = "First name needs to be between 2 and 20 characters.") String firstName) {
        this.firstName = firstName;
    }

    public @Size(min = 2, max = 20, message = "Last name needs to be between 2 and 20 characters.") String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 2, max = 20, message = "Last name needs to be between 2 and 20 characters.") String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}