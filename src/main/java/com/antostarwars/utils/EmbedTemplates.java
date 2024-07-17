package com.antostarwars.utils;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedTemplates {

    public static EmbedBuilder getBlacklistEmbed() {
        return new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("You're Blacklisted.")
                .setDescription("You're Blacklisted from using this Feature. \nPlease Contact the Staff if you think this was an error.");
    }

    public static EmbedBuilder getErrorEmbed(String error) {
        return new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("Something went wrong")
                .setDescription(error);
    }

    public static EmbedBuilder getSuccessEmbed(String success) {
        return new EmbedBuilder()
                .setColor(ColorPalette.getDiscordEmbed())
                .setTitle("Action made with success.")
                .setDescription(success);
    }
}
