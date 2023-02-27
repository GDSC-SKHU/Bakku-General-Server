package com.gdsc.bakku.event.domain.entity;

import com.gdsc.bakku.common.entity.BaseTimeEntity;
import com.gdsc.bakku.common.entity.Location;
import com.gdsc.bakku.event.dto.EventDTO;
import com.gdsc.bakku.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;

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

    @Embedded
    private Location location;

    @OneToOne
    @JoinColumn(name = "title_image_id")
    private Image image;

    public EventDTO toDTO() {
        return EventDTO.builder()
                .id(id)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .comment(comment)
                .latitude((location != null) ? location.getLatitude() : null)
                .longitude((location != null) ? location.getLongitude() : null)
                .imageUrl((image != null) ? image.getImageUrl() : null)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }
}
