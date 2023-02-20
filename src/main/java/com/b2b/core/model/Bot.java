package com.b2b.core.model;

import com.pengrad.telegrambot.TelegramBot;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Bot {
    private BotFile botFile;
    private TelegramBot telegramBot;
}
