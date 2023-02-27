package com.gdsc.bakku.ocean.controller;

import com.gdsc.bakku.ocean.dto.OceanDTO;
import com.gdsc.bakku.ocean.service.OceanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OceanController {

    private final OceanService oceanService;

    @GetMapping("/oceans")
    public ResponseEntity<Slice<OceanDTO>> findAll(@RequestParam(name = "lat") Double latitude,
                                               @RequestParam(name = "lon") Double longitude,
                                               @PageableDefault(size = 5) Pageable pageable) {
        Slice<OceanDTO> oceans = oceanService.findAll(latitude, longitude, pageable);

        if (oceans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(oceans);
    }

    @GetMapping("/oceans/{id}")
    public ResponseEntity<OceanDTO> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(oceanService.findById(id));
    }

}
