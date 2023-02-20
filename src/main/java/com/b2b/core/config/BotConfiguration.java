package com.b2b.core.config;

import com.b2b.core.model.Bot;
import com.b2b.core.model.BotFile;
import com.b2b.core.repository.BotFileRepository;
import com.b2b.core.repository.TelegramBotFactory;
import com.b2b.core.service.UpdateListenerImpl;
import com.b2b.core.service.UpdateResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final TelegramBotFactory telegramBotFactory;
    private final BotFileRepository botFileRepository;
    private final UpdateResolver updateResolver;

    @Bean
    public Map<?, ?> botImplementation() {
        List<BotFile> all = botFileRepository.getAll();
        Map<String, Bot> collect = all.stream().map(it -> Bot.builder()
                        .botFile(it)
                        .telegramBot(telegramBotFactory.getTelegramBot(it))
                        .build())
                .collect(Collectors.toMap(
                        it -> it.getTelegramBot().getToken(),
                        it -> it));

        collect.values().forEach(it -> it.getTelegramBot().setUpdatesListener(new UpdateListenerImpl(it, updateResolver)));

        return collect;
    }
}