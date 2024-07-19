package com.antostarwars.ticket.listeners;

import com.antostarwars.Bot;
import com.antostarwars.ticket.Ticket;
import com.antostarwars.ticket.TicketManager;
import com.antostarwars.utils.Environment;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TicketUpdateMessages extends ListenerAdapter {
    @Inject
    private Bot instance;

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("EEE, dd MMM yyyy HH:mm:ss z")
            .toFormatter()
            .withZone(ZoneId.of("Europe/Rome"));

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));
        TicketManager ticketManager = instance.getTicketManager();
        Ticket ticket = ticketManager.findBy(event.getChannel().getId());
        if (!event.getGuild().equals(guild) || !event.getChannelType().equals(ChannelType.TEXT) || ticket == null) return;
        if (event.getAuthor().isBot()) return;

        ticket.getMessagesTranscript().add(event.getAuthor().getName() + " [" + event.getMessage().getTimeCreated().format(formatter) + "] " + ": " + event.getMessage().getContentRaw() + "\n");
        if (!event.getMessage().getAttachments().isEmpty()) {
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                ticket.getMessagesTranscript().add(attachment.getProxyUrl() + "\n");
            }
        }

        ticket.updateMessagesNumber();
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (!event.isFromGuild()) return;

        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));
        TicketManager ticketManager = instance.getTicketManager();
        Ticket ticket = ticketManager.findBy(event.getChannel().getId());
        if (!event.getGuild().equals(guild) || !event.getChannelType().equals(ChannelType.TEXT) || ticket == null) return;
        if (event.getAuthor().isBot()) return;

        ticket.getMessagesTranscript().set(ticket.getMessagesTranscript().size() -1, event.getAuthor().getName() + " [" + event.getMessage().getTimeEdited().format(formatter) + "] " + ": " + event.getMessage().getContentRaw() + " (EDITED)" + "\n");
    }
}
