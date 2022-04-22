package ru.gitadded.diary_bot.services.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandlerService {
    public SendMessage handleCommandMessage(Message message);
    public SendMessage handleNonCommandMessage(Message message);
}
