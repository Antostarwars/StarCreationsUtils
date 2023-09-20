package com.antostarwars.profile;

import com.antostarwars.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProfileFixOldDatabase extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Command(
            name = "fixdatabase",
            description = "Fix Database Users"
    )
    public void onFixDatabase(SlashCommandInteractionEvent event) {
        List<Member> members = event.getGuild().getMembers();
        MongoCollection<Document> userCollection = instance.getMongo().getUsersCollection();
        ProfileManager profileManager = instance.getProfileManager();

        List<String> fixedMembers = new ArrayList<String>();
        for (Member member : members) {
            if (member.getUser().isBot()) continue;
            Document document = userCollection.find(Filters.eq("_id", member.getId())).first();
            if (document == null) {
                profileManager.addProfileAndUpdate(new Profile(member.getId(),member.getEffectiveName(),0));
                fixedMembers.add(member.getId());
            }
        }

        StringBuilder message = new StringBuilder();
        for (String id : fixedMembers) {
            message.append(event.getJDA().getUserById(id).getAsMention()).append("\n");
        }
        event.reply("Fixed Members: \n" + message.toString()).setEphemeral(true).queue();
    }
}
