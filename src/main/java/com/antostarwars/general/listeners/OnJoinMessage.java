package com.antostarwars.general.listeners;

import com.antostarwars.utils.Environment;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnJoinMessage extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("⭐ | Welcome!")
                .setDescription("Welcome " + event.getMember().getAsMention() + "!\nEnjoy your stay and check <#1068065149413511212>!");

        TextChannel channel = event.getJDA().getTextChannelById(Environment.get("WELCOME_CHANNEL_ID"));
        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
