package com.gdsc.bakku.ocean.service;

import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.repo.OceanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OceanService {

    private final OceanRepository oceanRepository;

    @Transactional(readOnly = true)
    public Slice<Ocean> findAll(Pageable pageable) {
        return oceanRepository.findAllBy(pageable);
    }

    @Transactional(readOnly = true)
    public Ocean findById(Long id) {
        return oceanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해변 존재하지 않음"));
    }
}
