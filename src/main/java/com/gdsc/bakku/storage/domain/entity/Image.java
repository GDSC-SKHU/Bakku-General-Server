package com.gdsc.bakku.storage.domain.entity;

import com.gdsc.bakku.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "original_name", nullable = false, length = 1000)
    private String originalName;

    @Column(name = "converted_name", nullable = false, length = 1000)
    private String convertedName;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;
}
