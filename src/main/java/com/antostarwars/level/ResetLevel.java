package com.antostarwars.level;

import com.antostarwars.Bot;
import com.antostarwars.profile.Profile;
import com.antostarwars.profile.ProfileManager;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;

public class ResetLevel extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Command(
            name = "resetlevel",
            description = "Reset level for a User",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onResetLevel(SlashCommandInteractionEvent event, User user) {
        if (user.isBot()) {
            event.reply("You cannot add level to a bot.").queue();
            return;
        }

        ProfileManager profileManager = instance.getProfileManager();

        profileManager.resetLevelAndUpdate(user.getId());
        Profile profile = profileManager.findBy(user.getId());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(user.getEffectiveName(), null, user.getAvatarUrl())
                .setDescription("**" + user.getEffectiveName() + "** level has been restored! (**Level " + profile.getLevel() + "**)")
                .setTimestamp(new Date().toInstant())
                .setFooter("Command issued by: " + event.getUser().getEffectiveName());

        event.replyEmbeds(builder.build()).queue();
    }
}
