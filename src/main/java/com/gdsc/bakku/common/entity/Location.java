package com.gdsc.bakku.common.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Embeddable
public class Location {

    private static final int EARTH_RADIUS = 6371;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    public Double calculateDistance(Location location) {
        Double targetLatitude = location.getLatitude();

        Double targetLongitude = location.getLongitude();

        Double latitudeDistance = toRadian(targetLatitude - this.latitude);

        Double longitudeDistance = toRadian(targetLongitude - this.longitude);

        Double arcSin = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2) +
                Math.cos(toRadian(this.latitude)) * Math.cos(toRadian(targetLatitude)) *
                        Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        Double angularDistance = 2 * Math.atan2(Math.sqrt(arcSin), Math.sqrt(1 - arcSin));

        return EARTH_RADIUS * angularDistance;
    }

    private Double toRadian(Double value) {
        return value * Math.PI / 180;
    }
}
