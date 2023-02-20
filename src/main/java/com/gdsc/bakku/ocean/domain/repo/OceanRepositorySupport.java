package com.gdsc.bakku.ocean.domain.repo;

import com.gdsc.bakku.common.entity.Location;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.entity.QOcean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OceanRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Ocean> findCloseOcean(Location location, Pageable pageable) {
        QOcean ocean = QOcean.ocean;

        return jpaQueryFactory.selectFrom(ocean)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(Expressions.stringTemplate("ST_Distance_Sphere({0}, {1})",
                        Expressions.stringTemplate("POINT({0}, {1})",
                                location.getLongitude(), location.getLatitude()),
                        Expressions.stringTemplate("POINT({0}, {1})",
                                ocean.location.longitude, ocean.location.latitude))
                        .asc())
                .fetch();
    }
}
