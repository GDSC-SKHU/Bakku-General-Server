package com.gdsc.bakku.group.domain.entity;

import com.gdsc.bakku.bakku.dto.response.GroupResponse;
import com.gdsc.bakku.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "user_group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Group extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public GroupResponse toDTO(Double totalWeight, Double totalCount) {
        return GroupResponse.builder()
                .id(id)
                .groupName(name)
                .totalWeight(totalWeight)
                .totalCount(totalCount)
                .createdDate(createdDate.toLocalDate())
                .build();
    }
}
