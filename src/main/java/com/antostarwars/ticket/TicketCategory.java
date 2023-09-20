package com.antostarwars.ticket;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public enum TicketCategory {
    TEXTURES("Textures Commission",
            "Need some Textures? (GUIs, Items, ...). Click Me!",
            Emoji.fromUnicode("🎨")),
    PLUGINS("Plugins Commission",
            "Need a Custom Plugin? Click Me!",
            Emoji.fromUnicode("💻")),
    CONFIGURATIONS("Configurations Commission",
            "Need some Plugin Configurations? Click Me!",
            Emoji.fromUnicode("⚙️")),
    MODELS("3D Models Commission",
            "Need some 3D Models? (Items, Entities, ...) Click Me!",
            Emoji.fromUnicode("⛏️")),
    DISCORD("Discord Commission",
            "Need Discord Help? (Discord Development, ...) Click Me!",
            Emoji.fromUnicode("🤖"));

    private final String name;
    private final String description;
    private final Emoji emoji;

    TicketCategory(String name, String description, Emoji emoji) {
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public static TicketCategory findBy(java.lang.String name) {
        for (TicketCategory category : TicketCategory.values()) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
}
