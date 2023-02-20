package com.b2b.core.repository;

import com.b2b.core.model.BotFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BotFileRepository {
    public List<BotFile> getAll() {
        File file = new File("src/main/resources/bots");
        File[] files = file.listFiles();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        List<BotFile> botFiles = new ArrayList<>();
        for (File file1 : files) {
            try {
                botFiles.add(objectMapper.readValue(file1, BotFile.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return botFiles;
    }
    @SneakyThrows
    public void flush(BotFile botFile) {
        File file = new File("src/main/resources/bots/config.json");
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        objectMapper.writeValue(file,botFile);
    }
}
