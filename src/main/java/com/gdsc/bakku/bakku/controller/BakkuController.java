package com.gdsc.bakku.bakku.controller;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.bakku.service.BakkuService;
import com.gdsc.bakku.common.annotation.CustomPageableAsQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
    @Operation(
            summary = "바꾸 한개 조회",
            description = "ID를 이용해서 바꾸를 하나 조회합니다",
            parameters = {
                    @Parameter(name = "id", description = "바꾸 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<BakkuResponse> findById(@PathVariable(name = "id") Long id) {
        BakkuResponse bakku = bakkuService.findById(id);

        return ResponseEntity.ok(bakku);
    }

    @GetMapping("/bakkus")
    @Operation(
            summary = "바꾸들을 조회합니다.",
            description = "바꾸들을 조회합니다. Request Param에 따라서 반환되는 요소들이 다릅니다.",
            parameters = {
                    @Parameter(name = "gid", description = "단체 ID"),
                    @Parameter(name = "oid", description = "바다 ID"),
                    @Parameter(name = "uid", description = "유저 아이디")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    @CustomPageableAsQueryParam
    public ResponseEntity<Slice<BakkuResponse>> findAll(
            @RequestParam(name = "gid", required = false)
            Long groupId,
            @RequestParam(name = "oid", required = false)
            Long oceanId,
            @RequestParam(name = "uid", required = false)
            String uid,
            @Parameter(hidden = true)
            @PageableDefault(size = 5)
            @SortDefault(value = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Slice<BakkuResponse> body = bakkuService.findAll(pageable, groupId, oceanId, uid);

        return ResponseEntity.ok(body);
    }

    @PostMapping(value = "/bakkus", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(
            summary = "바꾸 저장",
            description = "이미지들과 필드들을 이용해 바꾸를 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", ref = "400")
            }
    )
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
    @Operation(
            summary = "바꾸 이미지 수정",
            description = "해당 ID의 바꾸에서 이미지들만 수정합니다.",
            parameters = {
                    @Parameter(name = "id", description = "바꾸 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "400", ref = "400"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<BakkuResponse> updateBakkuImages(@PathVariable(name = "id") Long id,
                                                           @AuthenticationPrincipal User user,
                                                           @Valid @ModelAttribute BakkuImageRequest bakkuImageRequest) {
        BakkuResponse bakku = bakkuService.updateBakkuImages(id, user, bakkuImageRequest);

        return ResponseEntity.ok(bakku);
    }

    @PatchMapping(value = "/bakkus/{id}")
    @Operation(
            summary = "바꾸 필드 수정",
            description = "해당 ID의 바꾸에서 필드들만 수정합니다.",
            parameters = {
                    @Parameter(name = "id", description = "바꾸 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "400", ref = "400"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<BakkuResponse> updateBakkuField(@PathVariable(name = "id") Long id,
                                                          @AuthenticationPrincipal User user,
                                                          @Valid @RequestBody BakkuFieldRequest bakkuFieldRequest) {

        BakkuResponse bakku = bakkuService.updateBakkuField(id, user, bakkuFieldRequest);

        return ResponseEntity.ok(bakku);
    }

    @DeleteMapping(value = "/bakkus/{id}")
    @Operation(
            summary = "바꾸 삭제",
            description = "해당 ID의 바꾸를 삭제합니다.",
            parameters = {
                    @Parameter(name = "id", description = "바꾸 ID", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Void> deleteBakku(@PathVariable(name = "id") Long id,
                                            @AuthenticationPrincipal User user) {
        bakkuService.deleteById(id, user);

        return ResponseEntity.ok().build();
    }
}
