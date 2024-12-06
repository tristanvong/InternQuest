package be.ehb.tristan.javaadvanced.internquest.repositories.user;

import be.ehb.tristan.javaadvanced.internquest.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {}