package com.gdsc.bakku.storage.domain.repo;

import com.gdsc.bakku.storage.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}