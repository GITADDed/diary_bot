package ru.gitadded.diary_bot.utility;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class BotStateCash {
    private Map<String, BotState> botStateMap;

    public BotStateCash() {
        botStateMap = new HashMap<>();
    }
}
