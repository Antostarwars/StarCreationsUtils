package com.antostarwars.feedback;

import com.antostarwars.Bot;
import com.antostarwars.ticket.Ticket;
import com.antostarwars.ticket.TicketManager;
import com.antostarwars.utils.Environment;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class FeedbackListeners extends ListenerAdapter {

    @Inject
    private Bot instance;

    private String stars = "";
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (!event.getComponentId().equals("feedback-rating")) return;
        stars = event.getValues().get(0);

        TextInput content = TextInput.create("feedback-content", "Feedback Content", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Content of the Feedback!")
                .build();

        Modal modal = Modal.create("feedback-review", "Write a feedback. (" + event.getValues().get(0) + " Stars)")
                .addActionRow(content)
                .build();
        event.replyModal(modal).queue();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getModalId().equals("feedback-review")) return;
        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));
        TextChannel textChannel = guild.getTextChannelById(Environment.get("FEEDBACK_CHANNEL_ID"));

        TicketManager ticketManager = instance.getTicketManager();
        int ticketID = Integer.parseInt(event.getMessage().getEmbeds().get(0).getFields().get(0).getValue());
        Ticket ticket = ticketManager.findBy(ticketID);

        String content = event.getValue("feedback-content").getAsString();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(0x654597)
                .setAuthor(event.getUser().getName() + " sent a feedback!", null, event.getJDA().getSelfUser().getAvatarUrl())
                .setThumbnail(event.getUser().getAvatarUrl())
                .addField("__Review__", ":star:".repeat(Math.max(0, Integer.parseInt(stars))) + "\n" + content, false)
                .addField("__Ticket Information__", "**Customer**: " + event.getJDA().getUserById(ticket.getUserId()).getAsMention() + "\n"
                + "**Category**: " + ticket.getCategoryName() + "\n"
                + "**Total Messages**: " + ticket.getMessagesNumber().toString(), false)
                .setTimestamp(new Date().toInstant());
        
        textChannel.sendMessageEmbeds(embed.build()).queue();
        event.getMessage().delete().queue();

        event.reply("Thanks for your Feedback!").setEphemeral(true).queue();
    }
}
