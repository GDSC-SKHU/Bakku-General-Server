package com.gdsc.bakku.bakku.service;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.domain.repo.BakkuRepository;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.common.exception.BakkuNotFoundException;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.service.GroupService;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.service.OceanService;
import com.gdsc.bakku.storage.domain.entity.Image;
import com.gdsc.bakku.storage.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BakkuService {

    private final BakkuRepository bakkuRepository;

    private final GroupService groupService;

    private final OceanService oceanService;

    private final ImageService imageService;



    @Transactional
    public BakkuResponse save(BakkuRequest bakkuRequest, User user) {
        Group group = groupService.findOrCreateEntity(bakkuRequest.getGroupName());

        Bakku bakku = Bakku.builder()
                .decorateDate(bakkuRequest.getDecorateDate())
                .cleanWeight(bakkuRequest.getCleanWeight())
                .comment(bakkuRequest.getComment())
                .ocean(oceanService.findEntityById(bakkuRequest.getOceanId()))
                .group(group)
                .titleImage(imageSave(bakkuRequest.getTitleImage()))
                .beforeImage(imageSave(bakkuRequest.getBeforeImage()))
                .afterImage(imageSave(bakkuRequest.getAfterImage()))
                .user(user)
                .build();

        Bakku saveBakku = bakkuRepository.save(bakku);

        return saveBakku.toDTO();
    }

    @Transactional(readOnly = true)
    public BakkuResponse findById(Long id) {
        return bakkuRepository.findById(id).orElseThrow(BakkuNotFoundException::new).toDTO();
    }

    @Transactional(readOnly = true)
    public Slice<BakkuResponse> findAllByGroupId(Long id, Pageable pageable) {
        Group group = groupService.findEntityById(id);
        return bakkuRepository.findAllByGroup(group, pageable).map(Bakku::toDTO);
    }

    @Transactional(readOnly = true)
    public Slice<BakkuResponse> findAllByOceanId(Long id, Pageable pageable) {
        Ocean ocean = oceanService.findEntityById(id);

        return bakkuRepository.findAllByOcean(ocean, pageable).map(Bakku::toDTO);
    }

    @Transactional
    public void deleteById(Long id) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        bakkuRepository.delete(bakku);

        imageService.deleteByEntity("bakku/", bakku.getTitleImage());

        imageService.deleteByEntity("bakku/", bakku.getAfterImage());

        if(bakku.getBeforeImage() != null){
            imageService.deleteByEntity("bakku/", bakku.getBeforeImage());
        }
    }

    @Transactional
    public BakkuResponse updateBakkuField(Long id, BakkuFieldRequest bakkuFieldRequest) {
        Ocean ocean = oceanService.findEntityById(bakkuFieldRequest.getOceanId());

        Group group = groupService.findOrCreateEntity(bakkuFieldRequest.getGroupName());

        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        bakku.update(bakkuFieldRequest.getComment(), bakkuFieldRequest.getCleanWeight(), bakkuFieldRequest.getDecorateDate());

        bakku.updateOcean(ocean);

        bakku.updateGroup(group);

        Bakku updateBakku = bakkuRepository.save(bakku);

        return updateBakku.toDTO();
    }

    @Transactional
    public BakkuResponse updateBakkuImages(Long id, BakkuImageRequest bakkuImageRequest) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        bakku.updateTitleImage(imageSave(bakkuImageRequest.getTitleImage()));

        bakku.updateBeforeImage(imageSave(bakkuImageRequest.getBeforeImage()));

        bakku.updateAfterImage(imageSave(bakkuImageRequest.getAfterImage()));

        Bakku updateBakku = bakkuRepository.save(bakku);

        return updateBakku.toDTO();
    }

    private Image imageSave(MultipartFile image) {
        return imageService.uploadImage("bakku/", image);
    }
}
