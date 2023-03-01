package com.gdsc.bakku.group.domain.repo;

import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.domain.entity.QGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupRepositorySupprot {

    private final JPAQueryFactory jpaQueryFactory;

    public Group findSameGroup(String name) {
        QGroup group = QGroup.group;

        return jpaQueryFactory.select(group)
                .from(group)
                .where(group.name.upper().eq(name.toUpperCase()))
                .fetchOne();
    }
}
