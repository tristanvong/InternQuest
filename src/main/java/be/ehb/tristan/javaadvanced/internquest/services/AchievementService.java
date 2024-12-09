package be.ehb.tristan.javaadvanced.internquest.services;

import be.ehb.tristan.javaadvanced.internquest.enums.AchievementEnum;
import be.ehb.tristan.javaadvanced.internquest.enums.Rarity;
import be.ehb.tristan.javaadvanced.internquest.models.Achievement;
import be.ehb.tristan.javaadvanced.internquest.models.User;
import be.ehb.tristan.javaadvanced.internquest.repositories.AchievementRepository;
import be.ehb.tristan.javaadvanced.internquest.repositories.UserRepository;
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

            ClassLoader classLoader = getClass().getClassLoader();
            String baseDirectory = "static/images/achievements/";
            String baseFileName = achievementEnum.getName().toLowerCase().replace(" ", "-");
            String[] supportedImageExtensions = {"svg", "jpg", "png", "jpeg"};
            String imagePath = null;

            for(String extension : supportedImageExtensions) {
                String filePath = baseDirectory + baseFileName + "." + extension;
                if(classLoader.getResource(filePath) != null) {
                    imagePath = "achievements/" + baseFileName + "." + extension;
                    break;
                }
            }

            if(imagePath == null) {
                imagePath = "achievements/default.svg";
            }
            achievement.setPathToImage(imagePath);
            achievementRepository.save(achievement);
        }

        Achievement finalAchievement = achievement;
        boolean alreadyHasAchievement = user.getAchievements().stream()
                .anyMatch(a -> a.getName().equals(finalAchievement.getName()));

        if (!alreadyHasAchievement) {
            user.getAchievements().add(achievement);
            userRepository.save(user);
        }
    }
}