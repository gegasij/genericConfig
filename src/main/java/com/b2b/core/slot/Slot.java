package com.b2b.core.slot;

import com.b2b.core.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class Slot {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userData;

    public static List<String> getSlotsTime(List<Slot> slots) {
        return slots.stream()
                .map(it ->
                        DateUtil.getTimeRange(
                                it.getStartDate().toLocalTime(),
                                it.getEndDate().toLocalTime()))
                .toList();
    }
}
