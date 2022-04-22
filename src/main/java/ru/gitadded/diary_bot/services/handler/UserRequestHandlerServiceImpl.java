package ru.gitadded.diary_bot.services.handler;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gitadded.diary_bot.services.keyboard.KeyboardCreatorService;
import ru.gitadded.diary_bot.utility.BotStateCash;

@Service
public class UserRequestHandlerServiceImpl implements UserRequestHandlerService {

    @Autowired
    private KeyboardCreatorService keyboardCreatorService;

    @Autowired
    private InputMessageHandlerService inputMessageHandlerService;

    @Override
    public SendMessage handleCallback(CallbackQuery callbackQuery) {
        return new SendMessage(callbackQuery.getMessage().getChatId().toString(), "Hi from command handler");
    }

    @Override
    public SendMessage handleInputMessage(Update update) throws TelegramApiException {
        SendMessage response;
        if (update.hasMessage()) {
            if (update.getMessage().hasText() && update.getMessage().getText().charAt(0) == '/') {
                response = inputMessageHandlerService.handleCommandMessage(update.getMessage());
            } else {
                response = inputMessageHandlerService.handleNonCommandMessage(update.getMessage());
            }
        } else {
            throw new TelegramApiException();
        }
        return response;
    }
}
