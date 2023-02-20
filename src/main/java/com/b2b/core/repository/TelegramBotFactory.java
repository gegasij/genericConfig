package com.b2b.core.repository;

import com.b2b.core.model.BotFile;
import com.b2b.core.service.UpdateListenerImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.GetUpdates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotFactory {

    public List<TelegramBot> getTelegramBots(List<BotFile> botFiles) {
        return botFiles.stream()
                .map(this::getTelegramBot)
                .toList();
    }
    public TelegramBot getTelegramBot(BotFile botFile) {
        return new TelegramBot(botFile.getBotToken());
    }
}
