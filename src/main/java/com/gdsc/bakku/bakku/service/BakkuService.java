package com.gdsc.bakku.bakku.service;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.domain.repo.BakkuRepository;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.common.exception.BakkuNotFoundException;
import com.gdsc.bakku.common.exception.UserNoPermissionException;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.service.GroupService;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.service.OceanService;
import com.gdsc.bakku.redis.service.RedisService;
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

    private final RedisService redisService;

    @Transactional
    public BakkuResponse save(User user, BakkuRequest bakkuRequest) {
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

        String groupId = group.getId().toString();

        String oceanId = bakkuRequest.getOceanId().toString();

        Double score = (double) bakkuRequest.getCleanWeight();

        addRanking(groupId, oceanId, score);

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
    public BakkuResponse updateBakkuField(Long id, User user, BakkuFieldRequest bakkuFieldRequest) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        if (!bakku.getUser().getId().equals(user.getId())) {
            throw new UserNoPermissionException();
        }

        Ocean ocean = oceanService.findEntityById(bakkuFieldRequest.getOceanId());

        Group group = groupService.findOrCreateEntity(bakkuFieldRequest.getGroupName());

        updateRanking(bakku, group, ocean, (double) bakkuFieldRequest.getCleanWeight());

        bakku.update(bakkuFieldRequest.getComment(), bakkuFieldRequest.getCleanWeight(), bakkuFieldRequest.getDecorateDate());

        bakku.updateOcean(ocean);

        bakku.updateGroup(group);

        return bakku.toDTO();
    }

    @Transactional
    public BakkuResponse updateBakkuImages(Long id, User user, BakkuImageRequest bakkuImageRequest) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        if (!bakku.getUser().getId().equals(user.getId())) {
            throw new UserNoPermissionException();
        }

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

    @Transactional
    public void deleteById(Long id, User user) {
        Bakku bakku = bakkuRepository.findById(id)
                .orElseThrow(BakkuNotFoundException::new);

        if (!bakku.getUser().getId().equals(user.getId())) {
            throw new UserNoPermissionException();
        }

        deleteRanking(bakku);

        bakkuRepository.delete(bakku);

        imagesDelete(bakku.getTitleImage(), bakku.getAfterImage(), bakku.getBeforeImage());
    }

    private void addRanking(String groupId, String oceanId, Double score) {
        redisService.ZsetAddOrUpdate("group_weight", groupId, score);

        redisService.ZsetAddOrUpdate("group_count", groupId, 1.0);

        redisService.ZsetAddOrUpdate("ocean_" + oceanId, groupId, score);
    }

    private void updateRanking(Bakku bakku, Group newGroup, Ocean newOcean, Double newScore) {
        String oldGroupName = bakku.getGroup().getName();

        String oldGroupId = bakku.getGroup().getId().toString();

        Double oldScore = (double) bakku.getCleanWeight();

        String newGroupId = newGroup.getId().toString();

        if (oldGroupName.equals(newGroup.getName())) {
            redisService.ZsetAddOrUpdate("group_weight", oldGroupId, newScore - oldScore);
        } else {
            redisService.ZsetAddOrUpdate("group_weight", oldGroupId, oldScore * (-1));

            redisService.ZsetAddOrUpdate("group_count", oldGroupId, -1.0);

            redisService.ZsetAddOrUpdate("group_weight", newGroupId, newScore);

            redisService.ZsetAddOrUpdate("group_count", newGroupId, 1.0);
        }

        String oldOceanId = bakku.getOcean().getId().toString();

        String newOceanId = newOcean.getId().toString();

        redisService.ZsetAddOrUpdate("ocean_" + oldOceanId, oldGroupId, oldScore * (-1));

        if (oldOceanId.equals(newOceanId) && oldGroupName.equals(newGroup.getName())) {
            redisService.ZsetAddOrUpdate("ocean_" + oldOceanId, oldGroupId, newScore);
        } else if (oldOceanId.equals(newOceanId) && !oldGroupName.equals(newGroup.getName())) {
            redisService.ZsetAddOrUpdate("ocean_" + oldOceanId, newGroupId, newScore);
        } else if (!oldOceanId.equals(newOceanId) && oldGroupName.equals(newGroup.getName())) {
            redisService.ZsetAddOrUpdate("ocean_" + newOceanId, oldGroupId, newScore);
        } else {
            redisService.ZsetAddOrUpdate("ocean_" + newOceanId, newGroupId, newScore);
        }
    }

    private void deleteRanking(Bakku bakku) {
        String groupId = bakku.getGroup().getId().toString();

        String oceanId = bakku.getOcean().getId().toString();

        Double cleanWeight = (double) bakku.getCleanWeight();

        redisService.ZsetAddOrUpdate("group_weight", groupId, cleanWeight * (-1));

        redisService.ZsetAddOrUpdate("group_count", groupId, -1.0);

        redisService.ZsetAddOrUpdate("ocean_" + oceanId, groupId, cleanWeight * (-1));
    }

    private void imagesDelete(Image... images) {
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
