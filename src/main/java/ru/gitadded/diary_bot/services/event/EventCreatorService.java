package ru.gitadded.diary_bot.services.event;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface EventCreatorService {
    public SendMessage startCreatingEvent(String text, String userId);
}
