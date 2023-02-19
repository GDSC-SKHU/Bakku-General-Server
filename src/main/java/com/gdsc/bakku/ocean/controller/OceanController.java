package com.gdsc.bakku.ocean.controller;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.service.OceanService;
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
public class OceanController {

    private final OceanService oceanService;

    @GetMapping("/oceans")
    public ResponseEntity<Slice<Ocean>> findAll(@PageableDefault(size = 5) Pageable pageable) {
        Slice<Ocean> oceans = oceanService.findAll(pageable);

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
