package com.antostarwars;

import com.antostarwars.profile.ProfileManager;
import com.antostarwars.ticket.TicketManager;
import com.antostarwars.utils.Environment;
import com.antostarwars.utils.Mongo;
import gg.flyte.neptune.Neptune;
import gg.flyte.neptune.annotation.Instantiate;
import gg.flyte.neptune.util.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.time.Instant;

public class Bot {
    private final Logger LOGGER = Neptune.LOGGER;

    private ProfileManager profileManager;
    private TicketManager ticketManager;

    private long uptime;

    private Mongo mongo;
    public void initializeBot() throws InterruptedException {

        // Initialize JDA Bot
        JDA bot = JDABuilder
                .createDefault(Environment.get("BOT_TOKEN"))
                .setActivity(Activity.playing(Environment.get("ACTIVITY")))
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build().awaitReady();


        // Initialize Neptune Framework
        Guild guild = bot.getGuildById(Environment.get("BOT_GUILD"));
        new Neptune.Builder(bot, this)
                .addGuilds(guild)
                .clearCommands(Boolean.parseBoolean(Environment.get("CLEAR_COMMANDS")))
                .registerAllListeners(true)
                .create();

        LOGGER.info("{} " + bot.getSelfUser().getName() + " is ON!");
        // Initialize MongoDB
        mongo = new Mongo();
        mongo.initializeMongoDatabase();
        LOGGER.info("{} MongoDB Initialized Correctly!");

        // Initialize ProfileManager
        profileManager = new ProfileManager(this);
        profileManager.initializeProfileManager();
        LOGGER.info("{} ProfileManager Initialized Correctly!");

        // Initialize TicketManager
        ticketManager = new TicketManager(this);
        ticketManager.initializeTicketManager();
        LOGGER.info("{} TicketManager Initialized Correctly!");

        // Set Uptime to Now.
        uptime = System.currentTimeMillis();
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public TicketManager getTicketManager() { return ticketManager; }

    public long getUptime() { return uptime; }

    public Mongo getMongo() { return mongo; }

    @Instantiate
    public Bot instance() {
        return this;
    }
}
