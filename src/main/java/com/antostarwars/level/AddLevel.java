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

public class AddLevel extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Command(
            name = "addlevel",
            description = "Bot will add level to your account.",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onAddlevel(SlashCommandInteractionEvent event, User user, Integer amount) {
        if (user.isBot()) {
            event.reply("You cannot add level to a bot.").queue();
            return;
        }

        ProfileManager profileManager = instance.getProfileManager();

        profileManager.addlevelAndUpdate(user.getId(), amount);
        Profile profile = profileManager.findBy(user.getId());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(user.getEffectiveName(), null, user.getAvatarUrl())
                .setDescription("**" + user.getEffectiveName() + "** has reached a new Level! (**Level " + profile.getLevel() + "**)")
                .setTimestamp(new Date().toInstant())
                .setFooter("Command issued by: " + event.getUser().getEffectiveName());

        event.replyEmbeds(builder.build()).queue();
    }
}
