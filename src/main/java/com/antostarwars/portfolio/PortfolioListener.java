package com.antostarwars.portfolio;

import com.antostarwars.Bot;
import com.antostarwars.utils.EmbedTemplates;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

public class PortfolioListener extends ListenerAdapter {
    @Inject
    private Bot instance;

    @Override
    public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
        if (!event.getButton().getId().contains("Portfolio")) return;
        String[] args = event.getButton().getId().split("-");


        String page = args[2].trim();
        String portfolioID = args[1].trim();

        PortfolioManager portfolioManager = instance.getPortfolioManager();
        Portfolio portfolio = portfolioManager.findBy(portfolioID);

        event.deferEdit().queue();
        event.getHook().editOriginal(MessageEditData.fromCreateData(portfolio.generateMessage(Integer.parseInt(page), event.getGuild().getMemberById(portfolioID)))).queue();
    }
}
