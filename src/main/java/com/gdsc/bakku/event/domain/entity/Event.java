package com.gdsc.bakku.event.domain.entity;

import com.gdsc.bakku.common.entity.BaseTimeEntity;
import com.gdsc.bakku.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "location")
    private Point location;

    @OneToOne
    @JoinColumn(name = "title_image_id")
    private Image image;
}
