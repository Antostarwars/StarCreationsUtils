package com.antostarwars.profile;

import com.antostarwars.Bot;
import com.antostarwars.Main;
import com.antostarwars.utils.Environment;
import com.antostarwars.utils.Mongo;
import com.mongodb.client.model.Filters;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

public class ProfileListener extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));
        if (!event.getGuild().equals(guild)) return;

        ProfileManager profileManager = instance.getProfileManager();
        profileManager.addProfileAndUpdate(new Profile(event.getMember().getId(), event.getMember().getEffectiveName(), 0));
    }
}
