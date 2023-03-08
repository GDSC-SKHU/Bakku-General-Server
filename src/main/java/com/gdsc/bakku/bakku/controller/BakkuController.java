package com.gdsc.bakku.bakku.controller;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.bakku.service.BakkuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<BakkuResponse> saveBakku(@AuthenticationPrincipal User user,
                                                   @Valid @ModelAttribute BakkuRequest bakkuRequest) {
        BakkuResponse bakku = bakkuService.save(user, bakkuRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bakku.id())
                .toUri();

        return ResponseEntity.created(uri).body(bakku);
    }

    @PostMapping(value = "/bakkus/{id}/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BakkuResponse> updateBakkuImages(@PathVariable(name = "id") Long id,
                                                           @AuthenticationPrincipal User user,
                                                           @Valid @ModelAttribute BakkuImageRequest bakkuImageRequest) {
        BakkuResponse bakku = bakkuService.updateBakkuImages(id, user, bakkuImageRequest);

        return ResponseEntity.ok(bakku);
    }

    @PatchMapping(value = "/bakkus/{id}")
    public ResponseEntity<BakkuResponse> updateBakkuField(@PathVariable(name = "id") Long id,
                                                          @AuthenticationPrincipal User user,
                                                          @Valid @RequestBody BakkuFieldRequest bakkuFieldRequest) {

        BakkuResponse bakku = bakkuService.updateBakkuField(id, user, bakkuFieldRequest);

        return ResponseEntity.ok(bakku);
    }

    @DeleteMapping(value = "/bakkus/{id}")
    public ResponseEntity<Void> deleteBakku(@PathVariable(name = "id") Long id,
                                            @AuthenticationPrincipal User user) {
        bakkuService.deleteById(id, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/{id}/bakkus")
    public ResponseEntity<Slice<BakkuResponse>> findAllByGroupId(@PathVariable(name = "id") Long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bakkuService.findAllByGroupId(id, pageable));
    }

    @GetMapping("/oceans/{id}/bakkus")
    public ResponseEntity<Slice<BakkuResponse>> findAllByOceanId(@PathVariable(name = "id") Long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bakkuService.findAllByOceanId(id, pageable));
    }


}
