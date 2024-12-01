package be.ehb.tristan.javaadvanced.internquest.repositories.user;

import be.ehb.tristan.javaadvanced.internquest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);
    Optional<User> findByUsername(String username);
}
