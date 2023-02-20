package com.b2b.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BotFile {
    private String botToken;
    private List<AnswerMessage> answerMessage;
    private Payment payment;
    private List<ChatFunction> chatFunction;
    private Admin admin;
}
