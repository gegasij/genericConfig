package com.b2b.core.model;

import com.b2b.core.slot.Slot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Admin {
    private Long chatId;
    private String tgUsername;
    private List<Slot> slots;
}
