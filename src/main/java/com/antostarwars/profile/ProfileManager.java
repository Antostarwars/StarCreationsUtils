package com.antostarwars.profile;

import com.antostarwars.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.flyte.neptune.Neptune;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ProfileManager {

    private final Bot instance;
    private final HashMap<String, Profile> profilesCache = new HashMap<>();
    private final MongoCollection<Document> usersCollection;

    public ProfileManager(Bot instance) {
        this.instance = instance;
        this.usersCollection = instance.getMongo().getUsersCollection();
    }
    public void initializeProfileManager() {
        usersCollection.find().forEach((Consumer<? super Document>) document -> {
            if (!document.containsKey("blacklist")) {
                document.append("blacklist", false);
                usersCollection.replaceOne(Filters.eq("_id", document.getString("_id")), document, new ReplaceOptions().upsert(true));
            }
        });

        usersCollection.find().forEach((Consumer<? super Document>) document -> {
            String id = document.getString("_id");
            String username = document.getString("username");
            Integer level = document.getInteger("level", 0);
            boolean blacklist = document.getBoolean("blacklist", false);

            profilesCache.put(id, new Profile(id, username, level, blacklist));
        });
    }

    public HashMap<String, Profile> getProfilesCache() {
        return profilesCache;
    }

    public void addProfileAndUpdate(Profile profile) {
        if (profilesCache.get(profile.getId()) != null) {
            Neptune.LOGGER.error("[Profile Database Add Action] Profile already exist.");
            return;
        }

        profilesCache.put(profile.getId(), profile);

        Document document = new Document();
        document.put("_id", profile.getId());
        document.put("username", profile.getUsername());
        document.put("level", profile.getLevel());
        document.put("blacklist", profile.getBlacklist());
        usersCollection.insertOne(document);
    }

    public void addlevelAndUpdate(String id, Integer amount) {
        Document document = usersCollection.find(Filters.eq("_id", id)).first();
        if (document != null && document.containsKey("level")) {
            // Edit Caches
            Profile profile = profilesCache.get(id);
            profile.addLevel(amount);

            // Edit Database
            Integer currentLevel = document.getInteger("level");
            document.replace("level", currentLevel + amount);
            usersCollection.replaceOne(Filters.eq("_id", id), document, new ReplaceOptions().upsert(true));
        }
    }

    public void resetLevelAndUpdate(String id) {
        Document document = usersCollection.find(Filters.eq("_id", id)).first();
        if (document != null && document.containsKey("level")) {
            // Edit Caches
            Profile profile = profilesCache.get(id);
            profile.removeLevel(profile.getLevel());

            // Edit Database
            Integer currentLevel = document.getInteger("level");
            document.replace("level", 0);
            usersCollection.replaceOne(Filters.eq("_id", id), document, new ReplaceOptions().upsert(true));
        }
    }

    public void toggleBlacklist(String id) {
        Document document = usersCollection.find(Filters.eq("_id", id)).first();
        if (document != null && document.containsKey("blacklist")) {
            // Edit Caches
            Profile profile = profilesCache.get(id);
            profile.toggleBlacklist();

            // Edit Database
            Boolean blacklist = document.getBoolean("blacklist");
            document.replace("blacklist", profile.getBlacklist());
            usersCollection.replaceOne(Filters.eq("_id", id), document, new ReplaceOptions().upsert(true));
        }
    }

    public Profile findBy(String id) {
        return profilesCache.get(id);
    }

    public Profile findBy(User user) {
        return findBy(user.getId());
    }
}
