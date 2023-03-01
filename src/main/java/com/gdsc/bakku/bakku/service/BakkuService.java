package com.gdsc.bakku.bakku.service;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.domain.repo.BakkuRepository;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
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

        MultipartFile beforeImage = bakkuRequest.getBeforeImage();
        MultipartFile afterImage = bakkuRequest.getAfterImage();

        Bakku bakku = Bakku.builder()
                .decorateDate(bakkuRequest.getDecorateDate())
                .cleanWeight(bakkuRequest.getCleanWeight())
                .comment(bakkuRequest.getComment())
                .ocean(oceanService.findEntityById(bakkuRequest.getOceanId()))
                .group(group)
                .titleImage(imageSave(bakkuRequest.getTitleImage()))
                .beforeImage((beforeImage == null || beforeImage.isEmpty()) ? null : imageSave(beforeImage))
                .afterImage(imageSave(afterImage))
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

        imagesDelete(bakku.getTitleImage(), bakku.getAfterImage(), bakku.getBeforeImage());
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

        return bakku.toDTO();
    }

    @Transactional
    public BakkuResponse updateBakkuImages(Long id, BakkuImageRequest bakkuImageRequest) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        Image[] imagesForDelete = new Image[3];

        if (bakkuImageRequest.getIsChangeTitle()) {
            imagesForDelete[0] = bakku.getTitleImage();
            bakku.updateTitleImage(imageSave(bakkuImageRequest.getTitleImage()));
        }

        if (bakkuImageRequest.getIsChangeBefore()) {
            imagesForDelete[1] = bakku.getBeforeImage();
            bakku.updateBeforeImage(imageSave(bakkuImageRequest.getBeforeImage()));
        }

        if (bakkuImageRequest.getIsChangeAfter()) {
            imagesForDelete[2] = bakku.getAfterImage();
            bakku.updateAfterImage(imageSave(bakkuImageRequest.getAfterImage()));
        }

        imagesDelete(imagesForDelete);

        Bakku updateBakku = bakkuRepository.save(bakku);

        return updateBakku.toDTO();
    }

    private void imagesDelete(Image ... images) {
        for (Image image : images) {
            if (image == null) continue;
            imageService.deleteByEntity("bakku/", image);
        }
    }

    private Image imageSave(MultipartFile image) {
        if (image == null || image.isEmpty()) return null;
        return imageService.uploadImage("bakku/", image);
    }
}
