package com.antostarwars.portfolio;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    final String userId;
    private final List<String> paths;

    public Portfolio(String userId, List<String> paths) {
        this.userId = userId;
        this.paths = paths;
    }

    public List<String> getPaths() {
        return paths;
    }

    public List<FileUpload> getFiles() {
        List<FileUpload> files = new ArrayList<>();

        for (String path : paths) {
            File file = new File(path);
            files.add(FileUpload.fromData(file));
        }

        return files;
    }

    public MessageCreateData generateMessage(int currentPage, Member member) {
        var files = getFiles();

        // Setup Buttons
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("Portfolio - " + member.getId() + "-" + (currentPage), currentPage + 1 + "/" + files.size()).asDisabled());
        // Add/Disable Left Arrow Button
        if (currentPage > 0) buttons.add(0, Button.success("Portfolio - " + member.getId() + "-" + (currentPage - 1), Emoji.fromUnicode("U+2B05")));
        else buttons.add(0, Button.success("Portfolio - " + member.getId() + "-" + (currentPage - 1), Emoji.fromUnicode("U+2B05")).asDisabled());
        // Add/Disable Right Arrow button
        if (currentPage + 1 < files.size()) buttons.add(Button.success("Portfolio - " + member.getId() + "-" + (currentPage + 1), Emoji.fromUnicode("U+27A1")));
        else buttons.add(Button.success("Portfolio - " + member.getId() + "-" + (currentPage + 1), Emoji.fromUnicode("U+27A1")).asDisabled());

        return new MessageCreateBuilder()
                .addContent("### You're viewing " + member.getEffectiveName() + " Portfolio.")
                .addFiles(files.get(currentPage))
                .addActionRow(buttons)
                .build();
    }
}
