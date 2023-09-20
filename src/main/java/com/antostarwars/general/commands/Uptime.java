package com.antostarwars.general.commands;

import com.antostarwars.Bot;
import com.antostarwars.utils.ColorPalette;
import com.sun.management.OperatingSystemMXBean;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class Uptime extends ListenerAdapter {
    @Inject
    private Bot instance;

    @Command(
            name = "botinfo",
            description = "Display Bot Information like Uptime and Ping."
    )
    public void onUptime(SlashCommandInteractionEvent event) {
        // Uptime Calculation
        long uptimeMillis = System.currentTimeMillis() - instance.getUptime();
        long uptimeSeconds = uptimeMillis / 1000;
        long uptimeMinutes = uptimeSeconds / 60;
        long uptimeHours = uptimeMinutes / 60;
        long uptimeDays = uptimeHours / 24;
        String timeFormat = String.format("%d Days, %d Hours, %d Minutes, %d Seconds",
                uptimeDays,
                (uptimeHours % 24),
                (uptimeMinutes % 60),
                (uptimeSeconds % 60)
                );

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(ColorPalette.getUltraViolet())
                .setTitle("__Bot Info__")
                .addField("🆙 | Uptime:", timeFormat, false)
                .addField("🏓 | Ping:", event.getJDA().getGatewayPing() + "ms", false);

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }
}
