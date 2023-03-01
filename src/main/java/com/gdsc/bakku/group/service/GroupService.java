package com.gdsc.bakku.group.service;

import com.gdsc.bakku.common.exception.GroupNotFoundException;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.domain.repo.GroupRepository;
import com.gdsc.bakku.group.domain.repo.GroupRepositorySupprot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupRepositorySupprot groupRepositorySupprot;

    @Transactional
    public Group findOrCreateEntity(String name) {
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

    @Transactional
    public Group findEntityById(Long id) {
        return groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }

}
