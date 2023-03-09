package com.gdsc.bakku.ocean.domain.repo;

import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.entity.QOcean;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class  OceanRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<Ocean> findCloseOcean(Double latitude, Double longitude, Pageable pageable) {
        QOcean qOcean = QOcean.ocean;
        List<Ocean> oceanList = jpaQueryFactory.selectFrom(qOcean)
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(
                        new CaseBuilder()
                                .when(qOcean.position.latitude.isNull())
                                .then(1)
                                .otherwise(0)
                                .asc(),
                        new CaseBuilder()
                                .when(qOcean.position.longitude.isNull())
                                .then(1)
                                .otherwise(0)
                                .asc(),
                        Expressions.stringTemplate("ST_Distance_Sphere({0}, {1})",
                                Expressions.stringTemplate("POINT({0}, {1})",
                                        longitude, latitude),
                                Expressions.stringTemplate("POINT({0}, {1})",
                                        qOcean.position.longitude, qOcean.position.latitude))
                        .asc()
                )
                .fetch();

        boolean hasNext = false;

        if (oceanList.size() > pageable.getPageSize()) {
            oceanList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(oceanList, pageable, hasNext);
    }
}
