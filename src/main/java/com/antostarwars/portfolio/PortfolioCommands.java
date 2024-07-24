package com.antostarwars.portfolio;

import com.antostarwars.Bot;
import com.antostarwars.utils.EmbedTemplates;
import com.antostarwars.utils.Environment;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Objects;

public class PortfolioCommands {
    @Inject
    private Bot instance;

    @Command(
            name = "addportfolio",
            description = "Adds an image to portfolio"
    )
    public void onAddPortfolio(SlashCommandInteractionEvent event, Message.Attachment image) {
        if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(Environment.get("STAFF_ROLE_ID")))) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You cannot use this command!").build()).setEphemeral(true).queue();
            return;
        }

        PortfolioManager manager = instance.getPortfolioManager();

        Portfolio portfolio = manager.findBy(event.getUser().getId());

        if (portfolio == null) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You don't have any portfolio. \nCreate one using `/createportfolio`").build()).setEphemeral(true).queue();
            return;
        }

        if (!manager.saveImage(image, portfolio)) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("Failed to add portfolio to portfolio. Attachment **must** be an **Image**").build()).setEphemeral(true).queue();
            return;
        }

        manager.update(portfolio);
        event.replyEmbeds(EmbedTemplates.getSuccessEmbed("Added image to portfolio Correctly!").build()).setEphemeral(true).queue();
    }

    @Command(
            name = "createportfolio",
            description = "Create a portfolio"
    )
    public void onCreatePortfolio(SlashCommandInteractionEvent event) {
        if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(Environment.get("STAFF_ROLE_ID")))) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You cannot use this command!").build()).setEphemeral(true).queue();
            return;
        }

        PortfolioManager manager = instance.getPortfolioManager();

        if (!manager.add(new Portfolio(event.getUser().getId(), new ArrayList<>()))) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You already have a portfolio.").build()).setEphemeral(true).queue();
            return;
        }

        event.replyEmbeds(EmbedTemplates.getSuccessEmbed("Created a portfolio").build()).setEphemeral(true).queue();
    }

    @Command(
            name = "removeportfolio",
            description = "Remove a portfolio"
    )
    public void onRemovePortfolio(SlashCommandInteractionEvent event) {
        if (!event.getMember().getRoles().contains(event.getGuild().getRoleById(Environment.get("STAFF_ROLE_ID")))) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You cannot use this command!").build()).setEphemeral(true).queue();
            return;
        }

        PortfolioManager manager = instance.getPortfolioManager();

        if (!manager.remove(event.getUser().getId())) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("You don't have a portfolio.").build()).setEphemeral(true).queue();
            return;
        }

        event.replyEmbeds(EmbedTemplates.getSuccessEmbed("Removed portfolio").build()).setEphemeral(true).queue();
    }

    @Command(
            name = "portfolio",
            description = "Get a Team Member Portfolio"
    )
    public void onPortfolio(SlashCommandInteractionEvent event, User teamMember) {
        Member member = Objects.requireNonNull(event.getGuild()).getMemberById(teamMember.getId());
        assert member != null;

        if (!member.getRoles().contains(Objects.requireNonNull(event.getGuild()).getRoleById(Environment.get("STAFF_ROLE_ID")))) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed("The user you selected isn't a Team Member!").build()).setEphemeral(true).queue();
            return;
        }

        PortfolioManager manager = instance.getPortfolioManager();
        Portfolio portfolio = manager.findBy(teamMember.getId());

        if (portfolio == null) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed(teamMember.getAsMention() + " does not have a portfolio.").build()).setEphemeral(true).queue();
            return;
        }

        var files = portfolio.getFiles();

        if (files.isEmpty()) {
            event.replyEmbeds(EmbedTemplates.getErrorEmbed(teamMember.getAsMention() + " portfolio is currently empty.").build()).setEphemeral(true).queue();
            return;
        }

        event.reply(portfolio.generateMessage(0, member)).setEphemeral(true).queue();
    }
}
