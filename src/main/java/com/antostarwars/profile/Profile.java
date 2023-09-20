package com.antostarwars.profile;

public class Profile {

    private final String id;
    private final String username;
    private Integer level;

    public Profile(String id, String username, Integer level) {
        this.id = id;
        this.username = username;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getLevel() {
        return level;
    }


    public void addLevel(Integer amount) {
        this.level += amount;
    }

    public void removeLevel(Integer amount) { this.level -= amount; }
}
