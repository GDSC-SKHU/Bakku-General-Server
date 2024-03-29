package com.gdsc.bakku.ocean.domain.entity;

import com.gdsc.bakku.common.entity.BaseTimeEntity;
import com.gdsc.bakku.common.entity.Position;
import com.gdsc.bakku.ocean.dto.OceanDTO;
import com.gdsc.bakku.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "ocean")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ocean extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Embedded
    private Position position;

    @OneToOne
    @JoinColumn(name = "title_image_id")
    private Image image;

    public OceanDTO toDTO() {
        return OceanDTO.builder()
                .id(id)
                .name(name)
                .comment(comment)
                .address(address)
                .latitude((position != null) ? position.getLatitude() : null)
                .longitude((position != null) ? position.getLongitude() : null)
                .imageUrl((image != null) ? image.getImageUrl() : null)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }
}
