package ru.gitadded.diary_bot.services.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.gitadded.diary_bot.models.Event;
import ru.gitadded.diary_bot.repositories.EventRepository;
import ru.gitadded.diary_bot.utility.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;


@Service
public class EventCreatorServiceImpl implements EventCreatorService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCreateCash eventCreateCash;

    @Autowired
    private BotStateCash botStateCash;

    @Override
    public SendMessage startCreatingEvent(String text, String userId) {
        Map<String, Event> eventCash = eventCreateCash.getEventCash();
        BotEventEnterState botState = (BotEventEnterState) botStateCash.getBotStateMap().get(userId);
        SendMessage response = new SendMessage(userId, "Unexpected command, please try restart by entering /start.");

        try {
            switch (botState) {
                case ENTER_EVENT_DATE_TIME:
                    eventCash.put(userId, createEventWithDate(text, userId));
                    response = new SendMessage(userId, "Enter remind date and time.");
                    botStateCash.getBotStateMap().put(userId, BotEventEnterState.ENTER_REMIND_DATE_TIME);
                    break;
                case ENTER_REMIND_DATE_TIME:
                    setEventRemindDateTime(text, userId);
                    response = new SendMessage(userId, "Enter description of event.");
                    botStateCash.getBotStateMap().put(userId, BotEventEnterState.ENTER_EVENT_DESCRIPTION);
                    break;
                case ENTER_EVENT_DESCRIPTION:
                    setEventDescription(text, userId);
                    botStateCash.getBotStateMap().put(userId, BotUtilState.START);
                    eventRepository.save(eventCash.get(userId));
                    response = new SendMessage(userId, "Ok, I remind you about it at " +
                            TimeConverter.formZonedTimeToSimpleString(eventCash.get(userId).getRemindTime()));
                    eventCash.remove(userId);
                    break;
            }
        } catch (ParseException e) {
            response = new SendMessage(userId, "Invalid entering date.\nPlease follow this example: year-month-day " +
                    "hour:minute");
        }
        return response;
    }

    private Event createEventWithDate(String text, String userId) throws ParseException {
        return Event.builder()
                .eventTime(stringToZonedDateTime(text))
                .userIdEventOwner(userId)
                .build();
    }

    private void setEventRemindDateTime(String text, String userId) throws ParseException {
        Map<String, Event> eventCash = eventCreateCash.getEventCash();
        eventCash.get(userId).setRemindTime(stringToZonedDateTime(text));
    }

    private void setEventDescription(String text, String userId) {
        Map<String, Event> eventCash = eventCreateCash.getEventCash();
        eventCash.get(userId).setDescription(text);
    }

    private ZonedDateTime stringToZonedDateTime(String text) throws ParseException {
        ZoneId timeZone = ZoneId.of("Europe/Moscow");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = format.parse(text).toInstant().atZone(timeZone)
                .toLocalDateTime();
        return ZonedDateTime.of(dateTime, timeZone);
    }
}
