package be.ehb.tristan.javaadvanced.internquest.basicsremovelater;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserOld, Long> {
    Optional<UserOld> findByUsername(String username);
    List<UserOld> findByUsernameContainingOrderByUsernameAsc(String partialUsername);
}
