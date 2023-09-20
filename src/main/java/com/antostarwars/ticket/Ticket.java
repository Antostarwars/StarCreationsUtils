package com.antostarwars.ticket;

import gg.flyte.neptune.Neptune;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Ticket {

    private final Integer id;
    private final String ChannelId;
    private final String userId;
    private final String categoryName;
    private final Date startDate;
    private Integer messagesNumber;
    private final List<String> messagesTranscript;

    public Ticket(Integer id ,String ChannelId, String userId, String categoryName, Date startDate, List<String> messagesTranscript) {
        this.id = id;
        this.ChannelId = ChannelId;
        this.userId = userId;
        this.categoryName = categoryName;
        this.startDate = startDate;

        this.messagesTranscript = messagesTranscript;
        if (messagesTranscript.isEmpty()) messagesTranscript.add("Bot - Conversation Start Here.\n");

        this.messagesNumber = messagesTranscript.size();
    }

    public Integer getId() {
        return id;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Integer getMessagesNumber() {
        return messagesNumber;
    }

    public List<String> getMessagesTranscript() {
        return messagesTranscript;
    }

    public File getMessagesTranscriptFile() {
        File file = new File(System.getProperty("user.dir") + File.separator + "tickets" + File.separator + ChannelId + "-" + userId + ".txt");

        try {
            if (file.createNewFile()) {
                Neptune.LOGGER.info("[Ticket Info] Messages Transcript File for: " + userId + " is created correclty!");

                try (FileWriter fileWriter = new FileWriter(file)) {
                    for (String message : messagesTranscript) {
                        fileWriter.append(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return file;
            }
        } catch (IOException e) {
            Neptune.LOGGER.error("[Ticket Info] Messages Transcript File got an error while creating it.\n");
            e.printStackTrace();
            return null;
        }

        return file;
    }

    public void updateMessagesNumber() { this.messagesNumber = messagesTranscript.size(); }
}
