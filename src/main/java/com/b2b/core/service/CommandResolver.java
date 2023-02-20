package com.b2b.core.service;


import com.b2b.core.model.AnswerMessage;
import com.b2b.core.model.BotFile;
import com.b2b.core.model.Invoice;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommandResolver {
    public Object resolveCommand(BotFile botFile,String command) {
        List<Pair<String, Invoice>> invoicePairs = botFile.getPayment().getInvoice().stream().map(it -> Pair.of(it.getInvoiceCommand(), it)).toList();
        List<Pair<String, AnswerMessage>> pairs = botFile.getAnswerMessage().stream().map(it -> Pair.of(it.getCommand(), it)).toList();
        Optional<Pair<String, AnswerMessage>> first = pairs.stream().filter(it -> it.getFirst().equals(command))
                .findFirst();
        if (first.isPresent()) {
            return first.get().getSecond();
        }
        Optional<Pair<String, Invoice>> first1 = invoicePairs.stream().filter(it -> it.getFirst().equals(command))
                .findFirst();
        if (first1.isPresent()) {
            return first1.get().getSecond();
        } else
            return null;
    }
}
