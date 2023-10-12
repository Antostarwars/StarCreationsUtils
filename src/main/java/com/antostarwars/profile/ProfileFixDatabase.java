package com.antostarwars.profile;

import com.antostarwars.Bot;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.flyte.neptune.annotation.Command;
import gg.flyte.neptune.annotation.Inject;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProfileFixDatabase extends ListenerAdapter {

    @Inject
    private Bot instance;

    @Command(
            name = "fixdatabase",
            description = "Fix Database Users",
            permissions = {Permission.ADMINISTRATOR}
    )
    public void onFixDatabase(SlashCommandInteractionEvent event) {
        List<Member> members = event.getGuild().getMembers();
        MongoCollection<Document> userCollection = instance.getMongo().getUsersCollection();
        ProfileManager profileManager = instance.getProfileManager();

        List<String> fixedMembers = new ArrayList<>();
        for (Member member : members) {
            if (member.getUser().isBot()) continue;
            Document document = userCollection.find(Filters.eq("_id", member.getId())).first();
            if (document == null) {
                profileManager.addProfileAndUpdate(new Profile(member.getId(),member.getEffectiveName(),0, false));
                fixedMembers.add(member.getId());
            }
        }

        StringBuilder message = new StringBuilder();
        for (String id : fixedMembers) {
            message.append(event.getJDA().getUserById(id).getAsMention()).append("\n");
        }
        event.reply("Fixed Members: \n" + message.toString()).setEphemeral(true).queue();


        event.reply("Ciao.").queue();
    }
}
