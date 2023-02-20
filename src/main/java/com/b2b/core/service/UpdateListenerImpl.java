package com.b2b.core.service;

import com.b2b.core.model.Bot;
import com.b2b.core.model.NewUpdateDto;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Builder
public class UpdateListenerImpl implements UpdatesListener {
    private final Bot bot;
    private final UpdateResolver updateResolver;

    @Override
    public int process(List<Update> updates) {
        long startTime = System.currentTimeMillis();
        Update update = updates.get(0);
        try {
            updateResolver.resolveUpdate(NewUpdateDto.builder()
                    .bot(bot)
                    .update(update)
                    .build()
            );
        } catch (Exception exception) {
            System.out.println(exception);
        }
        System.out.println("time processing " + ((float) (System.currentTimeMillis() - startTime)) / 1000);
        return update.updateId();
    }
}

