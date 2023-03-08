package com.gdsc.bakku.rank.controller;

import com.gdsc.bakku.bakku.dto.response.GroupResponse;
import com.gdsc.bakku.rank.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/ranking/group")
    public ResponseEntity<List<GroupResponse>> findGroupRanking(@RequestParam(value = "sort", defaultValue = "weight") String sort) {
        if (sort.equals("count")) {
            return ResponseEntity.ok(rankingService.findAllByCount());
        }

        return ResponseEntity.ok(rankingService.findAllByWeight());
    }

    @GetMapping("/ranking/ocean/{id}")
    public ResponseEntity<List<GroupResponse>> findRankingByOceanId(@PathVariable(name = "id") Long id) {
        List<GroupResponse> rankingByOceanId = rankingService.findRankingByOceanId(id);

        return ResponseEntity.ok(rankingByOceanId);
    }
}
