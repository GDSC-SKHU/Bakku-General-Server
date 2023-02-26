package com.gdsc.bakku.ocean.service;

import com.gdsc.bakku.common.entity.Location;
import com.gdsc.bakku.common.exception.OceanNotFoundException;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.repo.OceanRepository;
import com.gdsc.bakku.ocean.domain.repo.OceanRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OceanService {

    private final OceanRepository oceanRepository;
    private final OceanRepositorySupport oceanRepositorySupport;

    @Transactional(readOnly = true)
    public List<Ocean> findAll(Double latitude, Double longitude, Pageable pageable) {
        Location location = new Location(longitude, latitude);

        return oceanRepositorySupport.findCloseOcean(location, pageable);
    }

    @Transactional(readOnly = true)
    public Ocean findById(Long id) {
        return oceanRepository.findById(id)
                .orElseThrow(OceanNotFoundException::new);
    }
}
