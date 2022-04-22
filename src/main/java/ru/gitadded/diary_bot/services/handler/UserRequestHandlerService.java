package ru.gitadded.diary_bot.services.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface UserRequestHandlerService {
    public SendMessage handleCallback(CallbackQuery callbackQuery);
    public SendMessage handleInputMessage(Update update) throws TelegramApiException;
}
