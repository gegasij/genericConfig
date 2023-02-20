package com.b2b.core.service;

import com.b2b.core.model.AnswerMessage;
import com.b2b.core.model.BotFile;
import com.b2b.core.util.BotUtil;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class BotFileService {
    private final CommandResolver commandResolver;

    public BaseRequest<?, ?> resolveMessage(BotFile from, Update update) {
        String command = BotUtil.getCommand(update);
        if (command != null) {
            Object o = commandResolver.resolveCommand(from, command);
            if (o instanceof AnswerMessage answerMessage) {

                SendMessage sendMessage = TelegramBotAdapter.adaptMessage(answerMessage, BotUtil.getChatId(update));

                List<InlineKeyboardButton> inlineKeyboardButtons = TelegramBotAdapter.getInlineKeyboardButtons(answerMessage);
                List<KeyboardButton> publicKeyboardButtons = TelegramBotAdapter.getPublicKeyboardButtons(from.getAnswerMessage());
                if (!inlineKeyboardButtons.isEmpty()) {
                    addKeyboard(sendMessage, inlineKeyboardButtons);
                } else if (!publicKeyboardButtons.isEmpty()) {
                    sendMessage.replyMarkup(new ReplyKeyboardMarkup(publicKeyboardButtons.toArray(new KeyboardButton[0])));
                }
                return sendMessage;
            }
        }
        return null;
    }

    private static void addKeyboard(SendMessage sendMessage, List<InlineKeyboardButton> inlineKeyboardButtons) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardButtons.forEach(keyboardMarkup::addRow);
        sendMessage.replyMarkup(keyboardMarkup);
    }

    public AnswerPreCheckoutQuery resolvePreCheckoutQuery(Update update) {
        return new AnswerPreCheckoutQuery(update.preCheckoutQuery().id());
    }
}

