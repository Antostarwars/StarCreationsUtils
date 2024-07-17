package com.antostarwars.portfolio;

import com.antostarwars.Bot;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.dv8tion.jda.api.entities.Message;
import org.bson.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PortfolioManager {

    final Bot instance;
    public PortfolioManager(Bot instance) {
        this.instance = instance;
    }

    public Portfolio findBy(String id) {
        var portfolioDb = instance.getMongo().getPortfolioCollection();

        Document document = portfolioDb.find(Filters.eq("_id", id)).first();

        if (document != null) {
            return new Portfolio(document.getString("_id"), document.getList("_paths", String.class, new ArrayList<>()));
        }

        return null;
    }

    public boolean add(Portfolio portfolio) {
        var portfolioDb = instance.getMongo().getPortfolioCollection();

        Document document = portfolioDb.find(Filters.eq("_id", portfolio.userId)).first();

        if (document != null) {
            return false;
        }

        document = new Document("_id", portfolio.userId).append("_paths", portfolio.getPaths());
        portfolioDb.insertOne(document);
        return true;
    }

    public boolean remove(String userId) {
        var portfolioDb = instance.getMongo().getPortfolioCollection();
        Document document = portfolioDb.find(Filters.eq("_id", userId)).first();

        if (document == null) {
            return false;
        }

        portfolioDb.deleteOne(document);
        String path = System.getProperty("user.dir") + File.separator + "portfolio"
                + File.separator + userId;

        // Removes every image and the portfolio directory.
        File dir = new File(path);
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            f.delete();
        }

        dir.delete();
        return true;
    }

    public boolean update(Portfolio portfolio) {
        var portfolioDb = instance.getMongo().getPortfolioCollection();
        Document document = portfolioDb.find(Filters.eq("_id", portfolio.userId)).first();

        if (document == null) {
            return false;
        }

        document.replace("_paths", portfolio.getPaths());
        instance.getMongo().getPortfolioCollection()
                .replaceOne(Filters.eq("_id", portfolio.userId), document, new ReplaceOptions().upsert(true));
        return true;
    }

    public boolean saveImage(Message.Attachment image, Portfolio portfolio) {
        if (!image.isImage()) return false;

        String path = System.getProperty("user.dir") + File.separator + "portfolio"
                + File.separator + portfolio.userId;
        String fileName = File.separator + portfolio.getPaths().size() + ".png";

        new File(path).mkdirs(); // Creates all the subdirectories.
        File file = new File(path + fileName);
        image.getProxy().downloadToFile(file);
        portfolio.getPaths().add(path + fileName); // Add file path to the database.

        return true;
    }
}
