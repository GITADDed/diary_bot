package ru.gitadded.diary_bot.utility;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.gitadded.diary_bot.models.Event;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class EventCreateCash {
    private Map<String, Event> eventCash = new HashMap<>();
}
