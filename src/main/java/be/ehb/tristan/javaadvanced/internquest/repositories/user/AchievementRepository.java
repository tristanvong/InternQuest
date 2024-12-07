package be.ehb.tristan.javaadvanced.internquest.repositories.user;

import be.ehb.tristan.javaadvanced.internquest.models.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findByName(String name);
}
