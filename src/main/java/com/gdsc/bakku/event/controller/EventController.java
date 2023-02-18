package com.gdsc.bakku.event.controller;

import com.gdsc.bakku.event.domain.entity.Event;
import com.gdsc.bakku.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<Slice<Event>> findAll(@PageableDefault(size = 5) Pageable pageable) {
        Slice<Event> events = eventService.findAll(pageable);

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> findById(@PathVariable(name = "id") Long id) {
        Event event = eventService.findById(id);

        return ResponseEntity.ok(event);
    }
}
