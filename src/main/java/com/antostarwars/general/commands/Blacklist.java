package com.antostarwars.general.commands;

import com.antostarwars.Bot;
import com.antostarwars.profile.Profile;
import com.antostarwars.profile.ProfileManager;
import com.antostarwars.utils.ColorPalette;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Description;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Blacklist extends ListenerAdapter {
    @Inject
    private Bot instance;

    @Command(
            name = "blacklist",
            description = "Toggle Blacklist for a User.",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onBlacklist(SlashCommandInteractionEvent event, @Description("Person to Toggle Blacklist") User user) {
        if (user.isBot()) {
            event.reply("User is a Bot.").setEphemeral(true).queue();
            return;
        }

        ProfileManager profileManager = instance.getProfileManager();
        Profile profile = profileManager.findBy(user);

        profileManager.toggleBlacklist(user.getId());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("🔒 | Toggle Blacklist")
                .addField("👤 | User:", user.getAsMention(), true)
                .addField("🛡️ | Blacklisted:", profile.getBlacklist() ? "Yes." : "No.", true);

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
