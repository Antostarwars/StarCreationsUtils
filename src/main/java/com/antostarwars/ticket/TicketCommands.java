package com.antostarwars.ticket;

import com.antostarwars.Bot;
import com.antostarwars.utils.ColorPalette;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class TicketCommands extends ListenerAdapter {
    @Inject
    private Bot instance;

    @Command(
            name = "ticketsetup",
            description = "Setup Ticket Embed.",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onTicketSetup(SlashCommandInteractionEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("<:tag:1149339814110445589> __Commission Tickets__")
                .setDescription("""
                        Do you need something **Custom** that look **Amazing**?
                        We provide you **many Services**!
                        
                        **Click** on a category to **open** a ticket.
                        """);

        StringSelectMenu categorySelectMenu = StringSelectMenu.create("ticket-choose")
                .addOption(
                        TicketCategory.TEXTURES.getName(),
                        TicketCategory.TEXTURES.getName(),
                        TicketCategory.TEXTURES.getDescription(),
                        TicketCategory.TEXTURES.getEmoji()
                )
                .addOption(
                        TicketCategory.PLUGINS.getName(),
                        TicketCategory.PLUGINS.getName(),
                        TicketCategory.PLUGINS.getDescription(),
                        TicketCategory.PLUGINS.getEmoji()
                )
                .addOption(
                        TicketCategory.CONFIGURATIONS.getName(),
                        TicketCategory.CONFIGURATIONS.getName(),
                        TicketCategory.CONFIGURATIONS.getDescription(),
                        TicketCategory.CONFIGURATIONS.getEmoji()
                )
                .addOption(
                        TicketCategory.MODELS.getName(),
                        TicketCategory.MODELS.getName(),
                        TicketCategory.MODELS.getDescription(),
                        TicketCategory.MODELS.getEmoji()
                )
                .addOption(
                        TicketCategory.DISCORD.getName(),
                        TicketCategory.DISCORD.getName(),
                        TicketCategory.DISCORD.getDescription(),
                        TicketCategory.DISCORD.getEmoji()
                )
                .setPlaceholder("Choose a Category!")
                .build();

        channel.sendMessageEmbeds(embed.build())
                .addActionRow(categorySelectMenu)
                .queue();

        event.reply("Ticket Embed Sent Correctly!").setEphemeral(true).queue();
    }
}
