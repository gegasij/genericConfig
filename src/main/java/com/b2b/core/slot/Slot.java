package com.b2b.core.slot;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Slot {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserData userData;
}
