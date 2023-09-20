package com.antostarwars.suggestions;

import com.antostarwars.utils.ColorPalette;
import com.antostarwars.utils.Environment;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SuggestionsListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        Guild guild = event.getJDA().getGuildById(Environment.get("BOT_GUILD"));
        TextChannel textChannel = event.getJDA().getTextChannelById(Environment.get("SUGGESTION_CHANNEL_ID"));

        if (!event.getGuild().equals(guild) || !event.getChannelType().equals(ChannelType.TEXT)|| !event.getChannel().asTextChannel().equals(textChannel)) return;
        if (event.getAuthor().isBot()) return;

        event.getMessage().delete().queue();

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("Suggestion")
                .setDescription(event.getMessage().getContentRaw())
                .addField("Submitted by", event.getAuthor().getAsMention(), false);

        textChannel.sendMessageEmbeds(embed.build()).queue(message -> {
            message.addReaction(Emoji.fromCustom("upvote", Long.parseLong(Environment.get("SUGGESTION_UPVOTE_EMOJI_ID")), false)).queue();
            message.addReaction(Emoji.fromCustom("downvote", Long.parseLong(Environment.get("SUGGESTION_DOWNVOTE_EMOJI_ID")), false)).queue();
            message.createThreadChannel("Discussion").queue();
        });
    }
}
