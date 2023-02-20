package com.b2b.core.slot;

import com.b2b.core.util.DateUtil;

import java.util.List;

public class SlotService {
    public static List<String> getSlotsDateButtons(List<Slot> slots) {
        return slots.stream()
                .map(it -> DateUtil.getDateFormat(it.getStartDate().toLocalDate()))
                .distinct()
                .toList();
    }

    public static String getSlotsTimeButtons(Slot slot) {
        return DateUtil.getTimeRange(slot.getStartDate().toLocalTime(), slot.getEndDate().toLocalTime());
    }
}
