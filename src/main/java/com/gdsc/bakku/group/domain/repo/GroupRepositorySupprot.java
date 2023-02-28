package com.gdsc.bakku.group.domain.repo;

import com.gdsc.bakku.bakku.domain.entity.QBakku;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.domain.entity.QGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroupRepositorySupprot {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<BakkuResponse> findBakkus(Long id, Pageable pageable) {
        QBakku bakku = QBakku.bakku;

        List<BakkuResponse> bakkus = jpaQueryFactory.select(Projections.constructor(BakkuResponse.class
                        , bakku.id
                        , bakku.comment
                        , bakku.cleanWeight
                        , bakku.decorateDate
                        , bakku.titleImage.imageUrl
                        , bakku.beforeImage.imageUrl
                        , bakku.afterImage.imageUrl
                        , bakku.group.name
                        , bakku.ocean.id
                        , bakku.ocean.name))
                .from(bakku)
                .limit(pageable.getPageSize()+1)
                .offset(pageable.getOffset())
                .where(bakku.group.id.eq(id))
                .fetch();

        boolean hasNext = false;

        if (bakkus.size() > pageable.getPageSize()) {
            bakkus.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(bakkus, pageable, hasNext);
    }

    public Group findSameGroup(String name) {
        QGroup group = QGroup.group;

        return jpaQueryFactory.select(group)
                .from(group)
                .where(group.name.eq(name))
                .fetchOne();
    }
}
