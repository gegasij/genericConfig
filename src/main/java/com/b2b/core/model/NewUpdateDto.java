package com.b2b.core.model;

import com.pengrad.telegrambot.model.Update;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewUpdateDto {
    private final Update update;
    private final Bot bot;
}
