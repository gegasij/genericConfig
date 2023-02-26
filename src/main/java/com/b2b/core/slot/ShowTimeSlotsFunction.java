package com.b2b.core.slot;


import com.b2b.core.model.ChatFunction;
import com.b2b.core.model.NewUpdateDto;
import com.b2b.core.service.BotFileService;
import com.b2b.core.service.KeyboardService;
import com.b2b.core.util.BotUtil;
import com.b2b.core.util.DateUtil;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShowTimeSlotsFunction {
    private final BotFileService botFileService;
    public static final String FUNCTION_NAME = "/slots";

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
            SendMessage sendMessage1 = withDateTime(newUpdateDto, localDateTime);
            newUpdateDto.getBot().getTelegramBot().execute(sendMessage1);
            newUpdateDto.getBot().getBotFile().getChatFunction().stream()
                    .filter(it -> it.getName().equals(FUNCTION_NAME))
                    .findFirst()
                    .map(ChatFunction::getSuccessCommand)
                    .map(it -> botFileService.resolveMessage(newUpdateDto.getBot().getBotFile(), it, BotUtil.getChatId(newUpdateDto.getUpdate())))
                    .ifPresent(it -> newUpdateDto.getBot().getTelegramBot().execute(it));
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
                .peek(it -> it.setUserData(UserData.builder().username(BotUtil.getTelegramUsername(newUpdateDto.getUpdate())).build()))
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
                                .callbackData(FUNCTION_NAME+" "+it.getStartDate().toString()))
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
                .map(it -> new InlineKeyboardButton(it).callbackData(FUNCTION_NAME+" "+it))
                .collect(Collectors.collectingAndThen(Collectors.toList(), KeyboardService::splitByButtons));

        InlineKeyboardMarkup keyboardMarkup = KeyboardService.getInlineKeyboard(collect);
        SendMessage sendMessage = new SendMessage(BotUtil.getChatId(newUpdateDto.getUpdate()), "Я разберусь с твоей проблемой и подготовлюсь к нашему разговору \n Когда будет удобно созвониться чтобы всё обсудить?");
        return sendMessage.replyMarkup(keyboardMarkup);
    }
}