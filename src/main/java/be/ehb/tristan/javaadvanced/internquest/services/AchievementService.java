package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.enums.AchievementEnum;
import be.ehb.tristan.javaadvanced.internquest.enums.Rarity;
import be.ehb.tristan.javaadvanced.internquest.models.Achievement;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.AchievementRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserRepository userRepository;

    public void checkAndAssignAchievement(User user, AchievementEnum achievementEnum, Rarity rarity) {
        Achievement achievement = achievementRepository.findByName(achievementEnum.getName());

        if (achievement == null) {
            achievement = new Achievement();
            achievement.setName(achievementEnum.getName());
            achievement.setDescription(achievementEnum.getDescription());
            achievement.setRarity(rarity);
            achievementRepository.save(achievement);
        }

        boolean alreadyHasAchievement = user.getAchievements().contains(achievement);

        if (!alreadyHasAchievement) {
            user.getAchievements().add(achievement);
            userRepository.save(user);
        }
    }
}