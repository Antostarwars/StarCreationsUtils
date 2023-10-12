package com.antostarwars.profile;

public class Profile {

    private final String id;
    private final String username;
    private Integer level;

    private boolean blacklist;

    public Profile(String id, String username, Integer level, boolean blacklist) {
        this.id = id;
        this.username = username;
        this.level = level;
        this.blacklist = blacklist;
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

    public boolean getBlacklist() { return blacklist; }


    public void addLevel(Integer amount) {
        this.level += amount;
    }

    public void removeLevel(Integer amount) { this.level -= amount; }

    public void toggleBlacklist() { this.blacklist = !this.blacklist; }
}
