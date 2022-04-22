package ru.gitadded.diary_bot.services.reminder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gitadded.diary_bot.bot.DiaryBot;
import ru.gitadded.diary_bot.models.Event;
import ru.gitadded.diary_bot.repositories.EventRepository;
import ru.gitadded.diary_bot.utility.TimeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RemindServiceImpl implements RemindService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemindServiceImpl.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DiaryBot diaryBot;

    @Override
    public void run() {
        List<Event> eventList;
        Instant nowUtc;
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime nowTime;
        while (LocalDateTime.now().getSecond() != 0) ;
        while (true) {
            nowUtc = Instant.now();
            nowTime = ZonedDateTime.ofInstant(nowUtc, zoneId);
            eventList = eventRepository.findAll();
            for (Event event : eventList) {
                if (nowTime.isBefore(event.getRemindTime().plusMinutes(1)) && nowTime.isAfter(event.getRemindTime())) {
                    try {
                        diaryBot.execute(new SendMessage(event.getUserIdEventOwner(), "Remind! At " +
                                TimeConverter.formZonedTimeToSimpleString(event.getEventTime()) +
                                " " + event.getDescription()));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    eventRepository.delete(event);
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(this).start();
        LOGGER.info("Reminder service start");
    }
}
