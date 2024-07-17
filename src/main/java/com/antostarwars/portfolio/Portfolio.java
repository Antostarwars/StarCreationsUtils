package com.antostarwars.portfolio;

import net.dv8tion.jda.api.utils.FileUpload;

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
            System.out.println(path);
            File file = new File(path);
            files.add(FileUpload.fromData(file));
        }

        return files;
    }
}
