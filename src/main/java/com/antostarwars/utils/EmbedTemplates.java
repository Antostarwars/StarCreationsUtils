package com.antostarwars.utils;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedTemplates {

    public static EmbedBuilder getBlacklistEmbed() {
        return new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("You're Blacklisted.")
                .setDescription("You're Blacklisted from using this Feature. \nPlease Contact the Staff if you think this was an error.");
    }
}
