package com.b2b.core.util;

import com.pengrad.telegrambot.model.Update;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BotUtil {
    public static Long getChatId(Update update) {
        if (update.callbackQuery() != null) {
            if (update.callbackQuery().message() != null) {
                return update.callbackQuery().message().chat().id();
            }
        } else if (update.message() != null) {
            return update.message().chat().id();
        } else if (update.preCheckoutQuery() != null) {
            if (update.preCheckoutQuery().from() != null) {
                return update.preCheckoutQuery().from().id();
            }
        }
        throw new RuntimeException("cant find chatID " + update);
    }

    public static String getTelegramUsername(Update update) {
        if (update.callbackQuery() != null
                && update.callbackQuery().message() != null
                && update.callbackQuery().message().chat() != null) {
            if (update.callbackQuery().message() != null) {
                return update.callbackQuery().message().chat().username();
            }
        } else if (update.message() != null && update.message().chat() != null) {
            return update.message().chat().username();
        }
        throw new RuntimeException("cant find chatID " + update);
    }

    public static String getCommand(Update update) {
        if (update.callbackQuery() != null && update.callbackQuery().data() != null) {
            return update.callbackQuery().data();
        }
        if (update.message() != null && update.message().text() != null) {
            return update.message().text();
        }
        return null;
    }

    public static String getParameter2(Update update) {
        String text = getCommand(update);
        if (text != null) {
            String[] split = text.split(" ");
            if (split.length >= 2) {
                return split[1];
            }
            return null;
        }
        return null;
    }
}
