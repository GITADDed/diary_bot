package ru.gitadded.diary_bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gitadded.diary_bot.models.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserIdEventOwner(String userId);
}
