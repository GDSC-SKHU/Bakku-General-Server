package com.gdsc.bakku.ocean.service;

import com.gdsc.bakku.common.exception.OceanNotFoundException;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.repo.OceanRepository;
import com.gdsc.bakku.ocean.domain.repo.OceanRepositorySupport;
import com.gdsc.bakku.ocean.dto.OceanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OceanService {

    private final OceanRepository oceanRepository;
    private final OceanRepositorySupport oceanRepositorySupport;

    @Transactional(readOnly = true)
    public Slice<OceanDTO> findAll(Double latitude, Double longitude, Pageable pageable) {
        return oceanRepositorySupport.findCloseOcean(latitude, longitude, pageable).map(Ocean::toDTO);
    }

    @Transactional(readOnly = true)
    public OceanDTO findById(Long id) {
        return findEntityById(id).toDTO();
    }

    @Transactional(readOnly = true)
    public Ocean findEntityById(Long id) {
        return oceanRepository.findById(id)
                .orElseThrow(OceanNotFoundException::new);
    }
}
