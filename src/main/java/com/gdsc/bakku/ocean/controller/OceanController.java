package com.gdsc.bakku.ocean.controller;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.service.OceanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OceanController {

    private final OceanService oceanService;

    @GetMapping("/oceans")
    public ResponseEntity<List<Ocean>> findAll(@RequestParam(name = "lat") Double latitude,
                                               @RequestParam(name = "lon") Double longitude,
                                               @PageableDefault(size = 5) Pageable pageable) {
        List<Ocean> oceans = oceanService.findAll(latitude,longitude,pageable);

        if (oceans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(oceans);
    }

    @GetMapping("/oceans/{id}")
    public ResponseEntity<Ocean> findById(@PathVariable(name = "id") Long id) {
        Ocean ocean = oceanService.findById(id);

        return ResponseEntity.ok(ocean);
    }

}
