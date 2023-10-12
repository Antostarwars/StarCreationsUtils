package com.antostarwars.profile.commands;

import com.antostarwars.Bot;
import com.antostarwars.profile.Profile;
import com.antostarwars.profile.ProfileManager;
import com.antostarwars.utils.ColorPalette;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ProfileLookup extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Command(
            name = "profile",
            description = "See your profile information",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onProfile(SlashCommandInteractionEvent event) {
        ProfileManager profileManager = instance.getProfileManager();
        Profile profile = profileManager.findBy(event.getUser());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("__PROFILE LOOKUP__")
                .setDescription("**👤 | Username: **" + event.getUser().getAsMention() +
                        "\n\n**⏫ | Level: **" + profile.getLevel() +
                        "\n\n**🎫 | Affiliate Code: **" + "sus" +
                        "\n\n**🪙 | Affiliate Earnings: **" + "104€");

        Button affiliateCode = Button.of(ButtonStyle.SUCCESS, "affiliate-set", "Set Affiliate Code", Emoji.fromUnicode("⚙"));

        event.replyEmbeds(embed.build())
                .addActionRow(affiliateCode)
                .setEphemeral(true)
                .queue();
    }
}
