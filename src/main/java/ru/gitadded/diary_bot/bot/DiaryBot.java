package ru.gitadded.diary_bot.bot;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import ru.gitadded.diary_bot.services.handler.UserRequestHandlerService;

/**
 * DiaryBot class that implements WebhookBot by extends SpringWebhookBot
 */
@Getter
@Setter
public class DiaryBot extends SpringWebhookBot {
    /**
     * Bot username getting by BotFather. Fill by @Value in configuration.
     */
    private String botUsername;
    /**
     * Bot token getting by BotFather. Fill by @Value in configuration.
     */
    private String botToken;
    /**
     * Bot path on server. Fill in Bean, which create this bot in configuration.
     */
    private String botPath;

    /**
     * Interface that will handle messages from users.
     */
    @Autowired
    UserRequestHandlerService userRequestHandlerService;

    /**
     * Constructor create DiaryBot with updating webhook address in local.
     *
     * @param setWebhook This parameter keep url send updates from telegram server.
     */
    public DiaryBot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    /**
     * Constructor create DiaryBot with custom or default options and updating webhook address in local.
     *
     * @param options    Telegram api object, that keep bot options.
     * @param setWebhook Telegram api object, that keep webhook address.
     */
    public DiaryBot(DefaultBotOptions options, SetWebhook setWebhook) {
        super(options, setWebhook);
    }

    /**
     * Method, that answer on command user or messages.
     *
     * @param update Telegram api object, that keep data about user, chat and users' message.
     * @return Bot answer. Expected, that it have SendMessage object with answer.
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SendMessage response;
        if (update != null && update.hasCallbackQuery()) {
            response = userRequestHandlerService.handleCallback(update.getCallbackQuery());
        } else {
            try {
                response = userRequestHandlerService.handleInputMessage(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                assert update != null;
                response = new SendMessage(update.getMessage().getChatId().toString(), "Telegram API Error.");
            }
        }
        return response;
    }
}
