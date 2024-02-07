package com.antostarwars.utils;

import com.antostarwars.Bot;
import gg.flyte.neptune.annotation.Inject;
import io.github.cdimascio.dotenv.Dotenv;

public class Environment {
    @Inject
    private Bot instance;

    private final static Dotenv dotenv = Dotenv.load();

    public static String get(String key) { return dotenv.get(key); }
}
