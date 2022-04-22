package ru.gitadded.diary_bot.configurations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;
import ru.gitadded.diary_bot.bot.DiaryBot;

/**
 * Configuration class, create beans to Spring container.
 */
@Configuration
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiaryBotConfig {
    /**
     * Url address for telegram server. This is address, which tg server will send http request with update from user.
     */
    @Value("${telegrambot.webhookPath}")
    String webHookPath;
    /**
     * Bot username in telegram system.
     */
    @Value ("${telegrambot.userName}")
    String userName;
    /**
     * Identification key in telegram system.
     */
    @Value("${telegrambot.botToken}")
    String botToken;

//    final String botPath = "";

//    @Value("${telegrambot.integral-url}")
//    String integralUrl;

    /**
     * That factory method create SetWebhook object with updating url for webhook address.
     * @return SetWebhook object with updating webhook url.
     */
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webHookPath).build();
    }

    /**
     * That factory method create DiaryBot object with fill up fields and registration bot session in telegram system.
     * @param setWebhookInstance SetWebhook object with updating webhook url for bot registration.
     * @return DiaryBot object.
     * @throws TelegramApiException
     */
    @Bean
    public DiaryBot diaryBot(SetWebhook setWebhookInstance) throws TelegramApiException {
        DiaryBot diaryBot = new DiaryBot(setWebhookInstance);
//        diaryBot.setBotPath(botPath);
        diaryBot.setBotToken(botToken);
        diaryBot.setBotUsername(userName);
        DefaultWebhook defaultWebhook = new DefaultWebhook();

        defaultWebhook.setInternalUrl("http://localhost");

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
        telegramBotsApi.registerBot(diaryBot, setWebhookInstance);
        return diaryBot;
    }
}
