package com.gdsc.bakku.bakku.domain.repo;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.domain.entity.QBakku;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BakkuRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBakku qBakku = QBakku.bakku;

    public Slice<Bakku> findAll(Group group, Ocean ocean, User user, Pageable pageable) {
        QBakku qBakku = QBakku.bakku;

        List<Bakku> bakkuList = jpaQueryFactory.selectFrom(qBakku)
                .where(
                        eqGroup(group),
                        eqOcean(ocean),
                        eqUser(user)
                )
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(qBakku.id.desc())
                .fetch();

        boolean hasNext = false;

        if (bakkuList.size() > pageable.getPageSize()) {
            bakkuList.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(bakkuList, pageable, hasNext);
    }

    private BooleanExpression eqGroup(Group group) {
        if (group == null) return null;
        return qBakku.group.eq(group);
    }

    private BooleanExpression eqOcean(Ocean ocean) {
        if (ocean == null) return null;
        return qBakku.ocean.eq(ocean);
    }

    private BooleanExpression eqUser(User user) {
        if (user == null) return null;
        return qBakku.user.eq(user);
    }
}
