package com.b2b.core.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

public class KeyboardService {
    public static final Integer defaultMenuColumns = 4;
    public static final Integer defaultMenuRows = 3;

    public static List<List<InlineKeyboardButton>> splitByButtons(List<InlineKeyboardButton> buttons) {
        return ListUtils.partition(buttons.stream().limit((long) defaultMenuRows * defaultMenuColumns).toList(), defaultMenuRows);
    }

    public static InlineKeyboardMarkup getInlineKeyboard(List<List<InlineKeyboardButton>> buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        buttons.stream().map(it -> it.toArray(new InlineKeyboardButton[0]))
                .forEach(inlineKeyboardMarkup::addRow);
        return inlineKeyboardMarkup;
    }
}
