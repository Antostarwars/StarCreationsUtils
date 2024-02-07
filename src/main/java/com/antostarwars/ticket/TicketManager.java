package com.antostarwars.ticket;

import com.antostarwars.Bot;
import com.antostarwars.utils.Environment;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.flyte.neptune.Neptune;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TicketManager {

    private final Bot instance;
    private final HashMap<String, Ticket> ticketsCache = new HashMap<>();

    private final MongoCollection<Document> ticketsCollection;

    public TicketManager(Bot instance) {
        this.instance = instance;
        this.ticketsCollection = instance.getMongo().getTicketsCollection();
    }

    public void initializeTicketManager() {
        List<TextChannel> tickets = instance.getJDA().getCategoryById(Environment.get("TICKET_CATEGORY")).getTextChannels();
        ticketsCollection.find().forEach( (Consumer<? super Document>) document -> {
            int id = document.getInteger("_id");
            String channelId = document.getString("channel_id");
            boolean ticketOpen = tickets.stream().anyMatch(element -> element.getId().equals(channelId));
            if (ticketOpen) {
                String userId = document.getString("user_id");
                String category = document.getString("category");
                Date startDate = document.getDate("start_date");
                List<String> messagesTranscript = document.getList("messages_transcript", String.class);
                Integer messagesNumber = document.getInteger("messages_number", 0);

                ticketsCache.put(channelId, new Ticket(id, channelId, userId, category, startDate, messagesTranscript));
            }
        });

        // Create Tickets Directory for Messages Transcript
        File dir = new File(System.getProperty("user.dir") + File.separator + "tickets");
        if (!dir.exists()) {
            if (dir.mkdir()){
                Neptune.LOGGER.info("[Ticket Manager] Tickets directory created.");
            } else {
                Neptune.LOGGER.error("[Ticket Manager] Ticket directory error while creating it.");
            }
        }

        // Start the TimerTask
        saveMessages();
    }

    public void addTicketAndUpdate(Ticket ticket) {
        if (ticketsCache.get(ticket.getChannelId()) != null) {
            Neptune.LOGGER.error("[Ticket Database Add Action] Ticket already exist.");
            return;
        }

        ticketsCache.put(ticket.getChannelId(), ticket);
        Document document = new Document();
        document.put("_id", ticket.getId());
        document.put("channel_id", ticket.getChannelId());
        document.put("user_id", ticket.getUserId());
        document.put("category", ticket.getCategoryName());
        document.put("start_date", ticket.getStartDate());
        document.put("messages_transcript", ticket.getMessagesTranscript());
        document.put("messages_number", ticket.getMessagesNumber());
        ticketsCollection.insertOne(document);
    }

    public int nextTicketId() { return (int) ticketsCollection.countDocuments() + 1; }
    public Ticket findBy(String channelId) { return ticketsCache.get(channelId); }

    public Ticket findBy(int id) {
        for (Ticket ticket : ticketsCache.values()) {
            if (ticket.getId() == id) return ticket;
        }
        return null;
    }

    public void saveMessages() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ticketsCollection.find().forEach( (Consumer<? super Document>) document -> {
                    String channelId = document.getString("channel_id");
                    Ticket ticket = findBy(channelId);

                    List<String> messagesTranscriptOld = document.getList("messages_transcript", String.class);
                    if (messagesTranscriptOld.equals(ticket.getMessagesTranscript())) return;

                    document.replace("messages_transcript", ticket.getMessagesTranscript());
                    document.replace("messages_number", ticket.getMessagesNumber());
                    ticketsCollection.replaceOne(Filters.eq("channel_id", channelId), document, new ReplaceOptions().upsert(true));
                });
            }
        };

        new Timer().scheduleAtFixedRate(task, 0 ,300000);
    }

    public void saveMessages(Integer id) {
        Document document = ticketsCollection.find(Filters.eq("_id", id)).first();
        if (document != null && document.containsKey("messages_transcript")) {
            Ticket ticket = findBy(id);
            document.replace("messages_transcript", ticket.getMessagesTranscript());
            document.replace("messages_number", ticket.getMessagesNumber());
            ticketsCollection.replaceOne(Filters.eq("_id", id), document, new ReplaceOptions().upsert(true));
        }
    }
}
