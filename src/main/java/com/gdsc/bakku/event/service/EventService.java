package com.gdsc.bakku.event.service;

import com.gdsc.bakku.event.domain.entity.Event;
import com.gdsc.bakku.event.domain.repo.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    @Transactional(readOnly = true)
    public Slice<Event> findAll(Pageable pageable) {
        return eventRepository.findAllBy(pageable);
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"이벤트 존재하지 않음"));
    }

}
