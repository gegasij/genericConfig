package com.b2b.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerMessage {
    private String command;
    @JsonProperty(value = "isPublic")
    private boolean isPublic;
    private String text;
    private List<MenuButton> menuButtons;
}
