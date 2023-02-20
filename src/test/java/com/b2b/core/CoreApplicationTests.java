package com.b2b.core;

import com.b2b.core.model.Admin;
import com.b2b.core.model.BotFile;
import com.b2b.core.slot.Slot;
import com.b2b.core.repository.BotFileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class CoreApplicationTests {
	@Autowired
	BotFileRepository botFileRepository;

	@Test
	void contextLoads() {
		List<BotFile> all = botFileRepository.getAll();
		BotFile botFile = all.get(1);
		Admin admin = new Admin();
		admin.setChatId(321L);
		admin.setTgUsername("321L");
		Slot slot = new Slot();
		slot.setStartDate(LocalDateTime.now());
		slot.setEndDate(LocalDateTime.now());
		Slot slot2= new Slot();
		slot2.setStartDate(LocalDateTime.now());
		slot2.setEndDate(LocalDateTime.now());
		botFile.setAdmin(admin);
		admin.setSlots(List.of(slot,slot2));
		botFileRepository.flush(botFile);
	}

}
