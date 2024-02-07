package com.antostarwars.ticket.listeners;

import com.antostarwars.Bot;
import com.antostarwars.ticket.Ticket;
import com.antostarwars.ticket.TicketManager;
import com.antostarwars.utils.ColorPalette;
import com.antostarwars.utils.EmbedTemplates;
import com.antostarwars.utils.Environment;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.TimeFormat;

import javax.swing.*;
import java.util.Date;

public final class TicketCloseCommand {

    @Inject
    private Bot instance;

    @Command(
            name = "close",
            description = "Close the ticket"
    )
    public void onTicketCloseCommand(SlashCommandInteractionEvent event) {
        TicketManager ticketManager = instance.getTicketManager();
        System.out.println(instance);
        System.out.println(ticketManager);

        Ticket ticket = ticketManager.findBy(event.getChannel().asTextChannel().getId());
        System.out.println(ticket);

        if(!event.getGuild().equals(event.getJDA().getGuildById(Environment.get("BOT_GUILD")))) return;
        if (ticket == null) event.replyEmbeds(EmbedTemplates.getErrorEmbed("You cannot use this command outside a ticket!").build()).queue();
        /*
        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));

        TextChannel ticketLogs = guild.getTextChannelById(Environment.get("TICKET_LOGS"));
        User ticketUser = event.getJDA().getUserById(ticket.getUserId());

        // Save Messages to Database
        ticketManager.saveMessages(ticket.getId());

        // Logs Embed
        EmbedBuilder logsEmbed = new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("Ticket Closed")
                .addField("<:open:1140366230356762733> Opened By", ticketUser.getAsMention(), true)
                .addField("<:close:1140366224384082020> Closed By", event.getUser().getAsMention(), true)
                .addField("<:opentime:1140366234840481852> Open Time", TimeFormat.RELATIVE.format(ticket.getStartDate().toInstant()), false)
                .addField("<:id:1140366226493804604> Ticket ID", ticket.getId().toString(), true)
                .addField("<:reason:1140366239202553876> Reason", "reason", true)
                .addBlankField(true)
                .addField("<:reason:1140366239202553876> Category", ticket.getCategoryName(), true)
                .addField("<:id:1140366226493804604> Messages Written", ticket.getMessagesNumber().toString(), true)
                .setTimestamp(new Date().toInstant());
        ticketLogs.sendMessageEmbeds(logsEmbed.build())
                .addFiles(FileUpload.fromData(ticket.getMessagesTranscriptFile()))
                .queue();

        // DM Feedback Embed
        EmbedBuilder feedbackEmbed = new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("__Ticket Closed__")
                .setDescription("""
                        Your Ticket has been closed in **__StarCreations__**
                        We would like to know how satisfied you are with **our services** by rating it with
                        **1-5** :star: below
                        """)
                .addField("Ticket ID", ticket.getId().toString(), false);
        StringSelectMenu categorySelectMenu = StringSelectMenu.create("feedback-rating")
                .addOption("1 Star", "1", Emoji.fromUnicode("⭐"))
                .addOption("2 Star", "2", Emoji.fromUnicode("⭐"))
                .addOption("3 Star", "3", Emoji.fromUnicode("⭐"))
                .addOption("4 Star", "4", Emoji.fromUnicode("⭐"))
                .addOption("5 Star", "5", Emoji.fromUnicode("⭐"))
                .setPlaceholder("Choose a Rating!")
                .build();

        ticketUser.openPrivateChannel().queue(user -> {
            user.sendMessageEmbeds(feedbackEmbed.build())
                    .addActionRow(categorySelectMenu)
                    .queue();
        });

        // Event Reply Embed
        TextChannel ticketChannel = event.getChannel().asTextChannel();
        ticketChannel.delete().queue();
        event.reply("Ticket Closed.").queue();
         */
    }
}
