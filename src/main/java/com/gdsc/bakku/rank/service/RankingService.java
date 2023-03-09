package com.gdsc.bakku.rank.service;

import com.gdsc.bakku.bakku.dto.response.GroupResponse;
import com.gdsc.bakku.common.exception.GroupNotFoundException;
import com.gdsc.bakku.group.domain.repo.GroupRepository;
import com.gdsc.bakku.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RedisService redisService;

    private final GroupRepository groupRepository;

    public List<GroupResponse> findAllByWeight(){
        Set<String> groupWeightRanking = redisService.getZset("group_weight", 4);

        return groupWeightRanking.stream()
                .map(groupId ->
                        groupRepository.findById(Long.parseLong(groupId))
                                .orElseThrow(GroupNotFoundException::new)
                                .toDTO(redisService.ZsetGetScore("group_weight", groupId), redisService.ZsetGetScore("group_count", groupId)))
                .collect(Collectors.toList());
    }

    public List<GroupResponse> findAllByCount() {
        Set<String> groupCountRanking = redisService.getZset("group_count", 4);

        return groupCountRanking.stream()
                .map(groupId ->
                        groupRepository.findById(Long.parseLong(groupId))
                                .orElseThrow(GroupNotFoundException::new)
                                .toDTO(redisService.ZsetGetScore("group_weight",groupId), redisService.ZsetGetScore( "group_count",groupId)))
                .collect(Collectors.toList());
    }

    public List<GroupResponse> findRankingByOceanId(Long id) {
        Set<String> oceanRanking = redisService.getZset("ocean_" + id, 9);

        return oceanRanking.stream()
                .map(groupId -> groupRepository.findById(Long.parseLong(groupId))
                        .orElseThrow(GroupNotFoundException::new)
                        .toDTO(redisService.ZsetGetScore("ocean_" + id, groupId), redisService.ZsetGetScore("group_count", groupId)))
                .collect(Collectors.toList());
    }
}
