package ru.gitadded.diary_bot.services.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gitadded.diary_bot.bot.DiaryBot;
import ru.gitadded.diary_bot.models.Event;
import ru.gitadded.diary_bot.repositories.EventRepository;
import ru.gitadded.diary_bot.utility.TimeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventViewerServiceImpl implements EventViewerService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DiaryBot diaryBot;

    @Override
    public SendMessage viewAllUserEvents(String userId) {
        SendMessage response;
        List<Event> eventList = eventRepository.findAllByUserIdEventOwner(userId);
        List<SendMessage> sendMessages;
        if (!eventList.isEmpty()) {
            if (eventList.size() > 1) {
                sendMessages = userEventToSendMessage(eventList);
                try {
                    for (int i = 0; i < sendMessages.size() - 1; i++) {
                        diaryBot.execute(sendMessages.get(i));
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                response = sendMessages.get(sendMessages.size() - 1);
            } else {
                response = userEventToSendMessage(eventList.get(0));
            }
        } else {
            response = new SendMessage(userId, "You aren't having events yet");
        }
        return response;
    }

    private static SendMessage userEventToSendMessage(Event event) {
        SendMessage response = new SendMessage(event.getUserIdEventOwner(), "");
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(event.getDescription()).append("\n");
        sb.append("Event time: ").append(TimeConverter.formZonedTimeToSimpleString(event.getEventTime())).append("\n");
        sb.append("Remind time: ").append(TimeConverter.formZonedTimeToSimpleString(event.getRemindTime())).append("\n");
        response.setText(sb.toString());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("Edit event time").callbackData("EDIT_EVENT_TIME").build();
        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
                .text("Edit remind time").callbackData("EDIT_REMIND_TIME").build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder()
                .text("Edit description").callbackData("EDIT_DESCRIPTION").build();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRaw = new ArrayList<>();
        keyboardRaw.add(button);
        keyboardRaw.add(button1);
        keyboardRaw.add(button2);
        keyboard.add(keyboardRaw);
        keyboardMarkup.setKeyboard(keyboard);
        response.setReplyMarkup(keyboardMarkup);
        return response;
    }

    private List<SendMessage> userEventToSendMessage(List<Event> event) {
        return event.stream().map(EventViewerServiceImpl::userEventToSendMessage).collect(Collectors.toList());
    }
}
