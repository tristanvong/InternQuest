package be.ehb.tristan.javaadvanced.internquest.repositories.user;

import be.ehb.tristan.javaadvanced.internquest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);
    User findByUsername(String username);
}
