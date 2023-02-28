package com.gdsc.bakku.group.controller;

import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.group.service.GroupService;
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
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/group/{id}/bakkus")
    public ResponseEntity<Slice<BakkuResponse>> findBakkusById(@PathVariable(name = "id") Long id, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(groupService.findBakkusById(id,pageable));
    }
}
