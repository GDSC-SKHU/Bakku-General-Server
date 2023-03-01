package com.gdsc.bakku.ocean.domain.repo;

import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.domain.entity.QOcean;
import com.gdsc.bakku.ocean.dto.OceanDTO;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class  OceanRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<OceanDTO> findCloseOcean(Double latitude, Double longitude, Pageable pageable) {
        QOcean ocean = QOcean.ocean;
        List<Ocean> oceanList = jpaQueryFactory.selectFrom(ocean)
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(
                        new CaseBuilder()
                                .when(ocean.position.latitude.isNull())
                                .then(1)
                                .otherwise(0)
                                .asc(),
                        new CaseBuilder()
                                .when(ocean.position.longitude.isNull())
                                .then(1)
                                .otherwise(0)
                                .asc(),
                        Expressions.stringTemplate("ST_Distance_Sphere({0}, {1})",
                                Expressions.stringTemplate("POINT({0}, {1})",
                                        longitude, latitude),
                                Expressions.stringTemplate("POINT({0}, {1})",
                                        ocean.position.longitude, ocean.position.latitude))
                        .asc()
                )
                .fetch();

        List<OceanDTO> oceanDTOS = new ArrayList<>();

        for (Ocean o : oceanList) {
            oceanDTOS.add(o.toDTO());
        }

        boolean hasNext = false;

        if (oceanDTOS.size() > pageable.getPageSize()) {
            oceanDTOS.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(oceanDTOS, pageable, hasNext);
    }
}
