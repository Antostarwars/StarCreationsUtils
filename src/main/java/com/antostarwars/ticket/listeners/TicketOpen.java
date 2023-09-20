package com.antostarwars.ticket.listeners;

import com.antostarwars.Bot;
import com.antostarwars.ticket.Ticket;
import com.antostarwars.ticket.TicketCategory;
import com.antostarwars.ticket.TicketManager;
import com.antostarwars.utils.ColorPalette;
import com.antostarwars.utils.Environment;
import gg.flyte.neptune.Neptune;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

public class TicketOpen extends ListenerAdapter {
    @Inject
    private Bot instance;

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (!event.getComponentId().equals("ticket-choose") || !event.getGuild().equals(event.getJDA().getGuildById(Environment.get("BOT_GUILD")))) return;

        TicketManager ticketManager = instance.getTicketManager();

        Category category = event.getJDA().getCategoryById(Environment.get("TICKET_CATEGORY"));

        event.deferReply(true).queue();

        EmbedBuilder ticketMessageEmbed = new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("<:cartshoppingsolid:1149341604348428338> __Welcome to Commission!__")
                .setDescription("""
                        
                        Please provide us all the info for the **Commission**!
                        One of the **Team Member** will respond as soon as possible!
                        
                        **Response Time**: 1 Hour Maximum""");
        Button closeButton = Button.of(ButtonStyle.DANGER, "ticket-close", "Close", Emoji.fromUnicode("U+1F512"));
        MessageCreateBuilder ticketMessage = new MessageCreateBuilder()
                .setContent("@everyone")
                .setEmbeds(ticketMessageEmbed.build())
                .addActionRow(closeButton);

        category.createTextChannel(event.getValues().get(0).split("Commission")[0].trim() + "-" + event.getUser().getName())
                .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_HISTORY), null)
                .queue(channel -> {
                    channel.sendMessage(ticketMessage.build()).queue();

                    ticketManager.addTicketAndUpdate(new Ticket(
                            ticketManager.nextTicketId(),
                            channel.getId(),
                            event.getUser().getId(),
                            event.getValues().get(0),
                            new Date(),
                            new ArrayList<>()));

                    EmbedBuilder ticketOpened = new EmbedBuilder()
                            .setColor(0x654597)
                            .setDescription("You opened a ticket correctly! " + channel.getAsMention());
                    event.getHook().editOriginalEmbeds(ticketOpened.build()).queue();
                });

    }
}
