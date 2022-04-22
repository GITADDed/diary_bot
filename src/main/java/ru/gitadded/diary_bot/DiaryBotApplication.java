package ru.gitadded.diary_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import ru.gitadded.diary_bot.services.reminder.RemindService;

@SpringBootApplication
public class DiaryBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryBotApplication.class, args);
    }

}
