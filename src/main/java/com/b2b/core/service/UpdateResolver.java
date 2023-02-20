package com.b2b.core.service;

import com.b2b.core.model.BotFile;
import com.b2b.core.model.NewUpdateDto;
import com.b2b.core.slot.ShowTimeSlotsFunction;
import com.b2b.core.util.BotUtil;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateResolver {
    private final BotFileService botFileService;
    private final ShowTimeSlotsFunction showTimeSlotsFunction;

    public void resolveUpdate(NewUpdateDto newUpdateDto) {
        Update update = newUpdateDto.getUpdate();
        BaseRequest<?, ?> request = null;
        if (BotUtil.getParameter1(update) != null) {
            if (BotUtil.getParameter1(update).equals(ShowTimeSlotsFunction.FUNCTION_NAME)) {
                showTimeSlotsFunction.getTimeslots(newUpdateDto);
            } else
                request = botFileService.resolveMessage(newUpdateDto.getBot().getBotFile(), BotUtil.getParameter1(update), BotUtil.getChatId(update));
        } else if (update.preCheckoutQuery() != null) {
            request = botFileService.resolvePreCheckoutQuery(update);
        }
        if (request != null) {
            newUpdateDto.getBot().getTelegramBot().execute(request);
        }
    }
}
