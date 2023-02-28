package com.gdsc.bakku.group.service;

import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.domain.repo.GroupRepository;
import com.gdsc.bakku.group.domain.repo.GroupRepositorySupprot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupRepositorySupprot groupRepositorySupprot;

    @Transactional
    public Group validateGroup(String name) {
        Group findGroup = groupRepositorySupprot.findSameGroup(name);

        if (findGroup == null) {

            Group group = Group.builder()
                    .name(name)
                    .build();

            return groupRepository.save(group);
        } else {
            return findGroup;
        }
    }

    @Transactional(readOnly = true)
    public Slice<BakkuResponse> findBakkusById(Long id, Pageable pageable) {
        return groupRepositorySupprot.findBakkus(id,pageable);
    }
}
