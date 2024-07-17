package com.antostarwars.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Mongo {
    private MongoClient client;

    private MongoDatabase database;

    private MongoCollection<Document> usersCollection;

    private MongoCollection<Document> ticketsCollection;

    private MongoCollection<Document> portfolioCollection;

    public void initializeMongoDatabase() {
        client = MongoClients.create(Environment.get("MONGO_URI"));
        database = client.getDatabase(Environment.get("MONGO_DATABASE"));
        usersCollection = database.getCollection(Environment.get("MONGO_USERS_COLLECTION"));
        ticketsCollection = database.getCollection(Environment.get("MONGO_TICKETS_COLLECTION"));
        portfolioCollection = database.getCollection(Environment.get("MONGO_PORTFOLIO_COLLECTION"));
    }

    public MongoCollection<Document> getUsersCollection() { return usersCollection; }

    public MongoCollection<Document> getTicketsCollection() { return ticketsCollection; }

    public MongoCollection<Document> getPortfolioCollection() { return portfolioCollection; }
}
