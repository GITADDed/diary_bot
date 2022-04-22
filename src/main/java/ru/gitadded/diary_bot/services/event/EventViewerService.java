package ru.gitadded.diary_bot.services.event;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface EventViewerService {
    SendMessage viewAllUserEvents(String userId);
}
