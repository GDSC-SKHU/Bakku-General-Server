package com.gdsc.bakku.event.domain.repo;

import com.gdsc.bakku.event.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}