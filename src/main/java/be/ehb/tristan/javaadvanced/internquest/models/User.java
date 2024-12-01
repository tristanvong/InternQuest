package be.ehb.tristan.javaadvanced.internquest.models;

import be.ehb.tristan.javaadvanced.internquest.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

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
    @Size(min = 8, max = 32, message = "Password needs to be between 8 and 32 characters long.")
    private String password;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "First name needs to be between 2 and 20 characters.")
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Last name needs to be between 2 and 20 characters.")
    private String lastName;

    @Column(nullable = false)
    private Enum<Role> role;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String profilePicturePath;

    @Column(nullable = true)
    private Long addressId;
}
