package ru.gitadded.diary_bot.services.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyboardCreatorService {
    InlineKeyboardMarkup createInlineKeyboard();
}
