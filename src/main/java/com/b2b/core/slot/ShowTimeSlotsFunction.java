package com.b2b.core.slot;


import com.b2b.core.model.ChatFunction;
import com.b2b.core.model.NewUpdateDto;
import com.b2b.core.service.KeyboardService;
import com.b2b.core.util.BotUtil;
import com.b2b.core.util.DateUtil;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ShowTimeSlotsFunction implements ChatFunction {
    private static final String FUNCTION_NAME = "/slots";

    @Override
    public String getFunctionName() {
        return FUNCTION_NAME;
    }

    public void getTimeslots(NewUpdateDto newUpdateDto) {
        String parameter2 = BotUtil.getParameter2(newUpdateDto.getUpdate());
        SendMessage sendMessage = null;
        if (parameter2 == null) {
            sendMessage = withoutParameters(newUpdateDto);
        }
        LocalDate localDate = DateUtil.isLocalDate(parameter2);
        if (localDate != null) {
            sendMessage = withOnlyDate(newUpdateDto, localDate);
        }
        LocalDateTime localDateTime = DateUtil.isLocalDateTime(parameter2);
        if (localDateTime != null) {
            sendMessage = withDateTime(newUpdateDto, localDateTime);
        }
        if (sendMessage != null) {
            newUpdateDto.getBot().getTelegramBot().execute(sendMessage);
        }
    }

    private SendMessage withDateTime(NewUpdateDto newUpdateDto, LocalDateTime localDateTime) {
        String s = newUpdateDto.getBot().getBotFile()
                .getAdmin()
                .getSlots()
                .stream()
                .filter(it -> it.getUserData() == null)
                .filter(it -> it.getStartDate().equals(localDateTime))
                .findAny()
                .stream()
                .peek(it -> it.setUserData(BotUtil.getTelegramUsername(newUpdateDto.getUpdate())))
                .map(it -> "Вы записались на сеанс %s в %s.".formatted(localDateTime.format(DateUtil.DATE_FORMATTER), localDateTime.format(DateUtil.TIME_FORMATTER)))
                .findAny()
                .orElse(null);
        if (s != null) {
            return new SendMessage(BotUtil.getChatId(newUpdateDto.getUpdate()), s);
        }
        return null;
    }

    private SendMessage withOnlyDate(NewUpdateDto newUpdateDto, LocalDate onlyDate) {
        List<Slot> availableSlots = newUpdateDto.getBot().getBotFile()
                .getAdmin()
                .getSlots().stream()
                .filter(it -> it.getUserData() == null)
                .filter(it -> it.getStartDate().toLocalDate().equals(onlyDate))
                .toList();

        List<List<InlineKeyboardButton>> collect = availableSlots.stream().map(it ->
                        new InlineKeyboardButton(SlotService.getSlotsTimeButtons(it))
                                .callbackData(it.getStartDate().toString()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), KeyboardService::splitByButtons));

        InlineKeyboardMarkup keyboardMarkup = KeyboardService.getInlineKeyboard(collect);
        SendMessage sendMessage = new SendMessage(BotUtil.getChatId(newUpdateDto.getUpdate()), "Выберите время");
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    private SendMessage withoutParameters(NewUpdateDto newUpdateDto) {
        List<Slot> availableSlots = newUpdateDto.getBot().getBotFile()
                .getAdmin()
                .getSlots()
                .stream().filter(it -> it.getUserData() == null)
                .filter(it -> LocalDateTime.now().plusHours(24).isBefore(it.getStartDate()))
                .toList();

        List<List<InlineKeyboardButton>> collect = SlotService.getSlotsDateButtons(availableSlots).stream()
                .map(it -> new InlineKeyboardButton(it).callbackData(it))
                .collect(Collectors.collectingAndThen(Collectors.toList(), KeyboardService::splitByButtons));

        InlineKeyboardMarkup keyboardMarkup = KeyboardService.getInlineKeyboard(collect);
        SendMessage sendMessage = new SendMessage(BotUtil.getChatId(newUpdateDto.getUpdate()), "Выберите время");
        return sendMessage.replyMarkup(keyboardMarkup);
    }
}