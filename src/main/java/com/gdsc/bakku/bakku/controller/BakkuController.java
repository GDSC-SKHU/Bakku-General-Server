package com.gdsc.bakku.bakku.controller;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.bakku.service.BakkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BakkuController {

    private final BakkuService bakkuService;

    @GetMapping("/bakkus/{id}")
    public ResponseEntity<BakkuResponse> findById(@PathVariable(name = "id") Long id) {
        BakkuResponse bakku = bakkuService.findById(id);

        return ResponseEntity.ok(bakku);
    }

    @PostMapping(value = "/bakkus", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BakkuResponse> saveBakku(@ModelAttribute BakkuRequest bakkuRequest, @AuthenticationPrincipal User user) {
        BakkuResponse bakku = bakkuService.save(bakkuRequest, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(bakku);
    }

    @PostMapping(value = "/bakkus/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BakkuResponse> updateBakkuImages(@PathVariable(name = "id") Long id, BakkuImageRequest bakkuImageRequest) {
        BakkuResponse bakku = bakkuService.updateBakkuImages(id, bakkuImageRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(bakku);
    }

    @PatchMapping(value = "/bakkus/{id}")
    public ResponseEntity<BakkuResponse> updateBakkuField(@PathVariable(name = "id") Long id, @RequestBody BakkuFieldRequest bakkuFieldRequest) {

        BakkuResponse bakku = bakkuService.updateBakkuField(id, bakkuFieldRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(bakku);
    }

    @DeleteMapping(value = "/bakkus/{id}")
    public ResponseEntity<Object> deleteBakku(@PathVariable(name = "id") Long id) {
        bakkuService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
