package com.b2b.core.service;


import com.b2b.core.model.AnswerMessage;
import com.b2b.core.model.Invoice;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendInvoice;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public class TelegramBotAdapter {
    public static SendInvoice adaptInvoice(Invoice invoice, Long chatId, String providerLink) {
        List<LabeledPrice> labeledPrices = invoice.getPrice().stream().map(it -> new LabeledPrice(it.getText(), it.getAmount())).toList();
        return new SendInvoice(chatId,
                invoice.getTitle(),
                invoice.getDescription(),
                invoice.getPayload(),
                providerLink,
                invoice.getCurrency(),
                labeledPrices.toArray(new LabeledPrice[]{}));
    }

    public static SendMessage adaptMessage(AnswerMessage answerMessage, Long chatId) {
        return new SendMessage(chatId, answerMessage.getText()).parseMode(ParseMode.HTML);
    }

    public static List<KeyboardButton> getPublicKeyboardButtons(List<AnswerMessage> answerMessages) {
        return answerMessages
                .stream()
                .filter(AnswerMessage::isPublic)
                .map(AnswerMessage::getText)
                .map(KeyboardButton::new).toList();
    }

    public static List<InlineKeyboardButton> getInlineKeyboardButtons(AnswerMessage answerMessage) {
        return answerMessage.getMenuButtons()
                .stream()
                .map(it -> {
                            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(it.getText())
                                    .callbackData(it.getCommand());
                            if (it.getLink() != null) {
                                inlineKeyboardButton.url(it.getLink());
                            }
                            return inlineKeyboardButton;
                        }
                )
                .toList();
    }
}
