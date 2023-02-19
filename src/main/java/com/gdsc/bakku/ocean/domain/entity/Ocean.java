package com.gdsc.bakku.ocean.domain.entity;

import com.gdsc.bakku.common.entity.BaseTimeEntity;
import com.gdsc.bakku.storage.domain.entity.Image;
import org.springframework.data.geo.Point;
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

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "location")
    private Point location;

    @OneToOne
    @JoinColumn(name = "title_image_id")
    private Image image;
}
