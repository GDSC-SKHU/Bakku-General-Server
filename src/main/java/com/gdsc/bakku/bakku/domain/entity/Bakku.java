package com.gdsc.bakku.bakku.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.storage.domain.entity.Image;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "bakku")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bakku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "clean_weight", nullable = false)
    private int cleanWeight;

    @Column(name = "decorate_date", nullable = false)
    private LocalDate decorateDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "title_image_id", referencedColumnName = "id")
    private Image titleImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "before_image_id", referencedColumnName = "id")
    private Image beforeImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "after_image_id", referencedColumnName = "id")
    private Image afterImage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ocean_id", nullable = false)
    private Ocean ocean;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public BakkuResponse toDTO() {
        return BakkuResponse.builder()
                .id(id)
                .comment(comment)
                .cleanWeight(cleanWeight)
                .decorateTime(decorateDate)
                .titleImageUrl(titleImage != null ? titleImage.getImageUrl() : null)
                .beforeImageUrl(beforeImage != null ? beforeImage.getImageUrl() : null)
                .afterImageUrl(afterImage != null ? afterImage.getImageUrl() : null)
                .groupName(group.getName())
                .oceanId(ocean.getId())
                .oceanName(ocean.getName())
                .build();
    }

    public void update(String comment, int cleanWeight, LocalDate decorateDate) {
        this.comment = comment;

        this.cleanWeight = cleanWeight;

        this.decorateDate = decorateDate;
    }

    public void updateOcean(Ocean ocean) {
        this.ocean = ocean;
    }

    public void updateGroup(Group group) {
        this.group = group;
    }

    public void updateTitleImage(Image titleImage) {
        this.titleImage = titleImage;
    }

    public void updateBeforeImage(Image beforeImage) {
        this.beforeImage = beforeImage;
    }

    public void updateAfterImage(Image afterImage) {
        this.afterImage = afterImage;
    }
}
