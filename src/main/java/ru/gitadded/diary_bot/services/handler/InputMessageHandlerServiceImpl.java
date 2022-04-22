package ru.gitadded.diary_bot.services.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gitadded.diary_bot.services.event.EventCreatorService;
import ru.gitadded.diary_bot.services.event.EventViewerService;
import ru.gitadded.diary_bot.utility.BotEventEnterState;
import ru.gitadded.diary_bot.utility.BotState;
import ru.gitadded.diary_bot.utility.BotStateCash;
import ru.gitadded.diary_bot.utility.BotUtilState;

import java.util.Map;

@Service
public class InputMessageHandlerServiceImpl implements InputMessageHandlerService {

    @Autowired
    private EventViewerService eventViewerService;

    @Autowired
    private BotStateCash botStateCash;

    @Autowired
    private EventCreatorService eventCreatorService;

    @Override
    public SendMessage handleCommandMessage(Message message) {
        SendMessage response = new SendMessage(message.getChatId().toString(), "Unexpected command!");
        if (message.getText().equals("/start")) {
            botStateCash.getBotStateMap().put(message.getChatId().toString(), BotUtilState.START);
            response = new SendMessage(message.getChatId().toString(), "Hello dear friend. What do we do today?");
        }
        if (message.getText().equals("/create_remind")) {
            botStateCash.getBotStateMap().put(message.getChatId().toString(), BotEventEnterState.ENTER_EVENT_DATE_TIME);
            response = new SendMessage(message.getChatId().toString(), "Enter date and time of event.\nExample:" +
                    " \"2020-04-15 17:30\".");
        }
        if (message.getText().equals("/view_my_events")) {
            response = eventViewerService.viewAllUserEvents(message.getChatId().toString());
        }
        return response;
    }

    @Override
    public SendMessage handleNonCommandMessage(Message message) {
        Map<String, BotState> botStates = botStateCash.getBotStateMap();
        String userId = message.getChatId().toString();
        SendMessage response = new SendMessage(userId, "You weren't entering anything events yet. Try /start.");

        if (botStates.containsKey(userId)) {
            BotState botState = botStates.get(userId);
            if (botState instanceof BotEventEnterState) {
                response = eventCreatorService.startCreatingEvent(message.getText(), userId);
                response.enableMarkdown(false);
            }
        } else {
            botStates.put(userId, BotUtilState.START);
        }
        return response;
    }
}
