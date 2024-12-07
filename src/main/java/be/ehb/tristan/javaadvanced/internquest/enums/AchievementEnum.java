package be.ehb.tristan.javaadvanced.internquest.enums;

public enum AchievementEnum {
    MADE_AN_ACCOUNT("Made an account", "You successfully made an account on InternQuest!"),
    CREATED_A_COMPANY("Created a company", "You successfully created a company on InterQuest!");

    private String name;
    private String description;

    AchievementEnum(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
