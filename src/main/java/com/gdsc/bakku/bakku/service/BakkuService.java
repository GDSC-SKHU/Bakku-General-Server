package com.gdsc.bakku.bakku.service;

import com.gdsc.bakku.auth.domain.entity.User;
import com.gdsc.bakku.bakku.domain.entity.Bakku;
import com.gdsc.bakku.bakku.domain.repo.BakkuRepository;
import com.gdsc.bakku.bakku.dto.request.BakkuFieldRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuImageRequest;
import com.gdsc.bakku.bakku.dto.request.BakkuRequest;
import com.gdsc.bakku.bakku.dto.response.BakkuResponse;
import com.gdsc.bakku.bakku.dto.response.GroupRankingResponse;
import com.gdsc.bakku.bakku.dto.response.GroupResponse;
import com.gdsc.bakku.common.exception.BakkuNotFoundException;
import com.gdsc.bakku.common.exception.GroupNotFoundException;
import com.gdsc.bakku.common.exception.UserNoPermissionException;
import com.gdsc.bakku.group.domain.entity.Group;
import com.gdsc.bakku.group.domain.repo.GroupRepository;
import com.gdsc.bakku.group.service.GroupService;
import com.gdsc.bakku.ocean.domain.entity.Ocean;
import com.gdsc.bakku.ocean.service.OceanService;
import com.gdsc.bakku.storage.domain.entity.Image;
import com.gdsc.bakku.storage.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BakkuService {

    private final BakkuRepository bakkuRepository;

    private final GroupService groupService;

    private final OceanService oceanService;

    private final ImageService imageService;

    private final StringRedisTemplate groupStringRedisTemplate;

    private final GroupRepository groupRepository;

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

        addRanking(group, bakku);

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

        updateRanking(bakkuFieldRequest, bakku, group);

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

    public GroupRankingResponse findAllGroupRanking() {
        Set<String> groupWeightRanking = getZset("group_weight", 4);

        List<GroupResponse> groupWeight = groupWeightRanking.stream()
                .map(groupId ->
                        groupRepository.findById(Long.parseLong(groupId))
                                .orElseThrow(GroupNotFoundException::new)
                                .toDTO(ZsetGetScore("group_weight",groupId), ZsetGetScore( "group_count",groupId)))
                .collect(Collectors.toList());

        Set<String> groupCountRanking = getZset("group_count", 4);

        List<GroupResponse> groupCount = groupCountRanking.stream()
                .map(groupId ->
                        groupRepository.findById(Long.parseLong(groupId))
                                .orElseThrow(GroupNotFoundException::new)
                                .toDTO(ZsetGetScore("group_weight",groupId), ZsetGetScore( "group_count",groupId)))
                .collect(Collectors.toList());

        return GroupRankingResponse.builder()
                .groupWeight(groupWeight)
                .groupCount(groupCount)
                .build();
    }

    public List<GroupResponse> findRankingByOceanId(Long id) {
        Set<String> oceanRanking = getZset("ocean_" + id, 9);

        return oceanRanking.stream()
                .map(groupId -> groupRepository.findById(Long.parseLong(groupId))
                        .orElseThrow(GroupNotFoundException::new)
                        .toDTO(ZsetGetScore("ocean_" + id, groupId), ZsetGetScore("group_count", groupId)))
                .collect(Collectors.toList());
    }

    private void addRanking(Group group, Bakku bakku) {
        Double presentWeight = ZsetGetScore("group_weight", bakku.getGroup().getId().toString());

        if ( presentWeight == null) {
            ZSetAdd("group_weight", group.getId().toString(), (double) bakku.getCleanWeight());
        } else {
            ZSetAdd("group_weight", group.getId().toString(), presentWeight + bakku.getCleanWeight());
        }

        Double presentCount = ZsetGetScore("group_count", bakku.getGroup().getId().toString());

        if (presentCount == null) {
            ZSetAdd("group_count", group.getId().toString(), 1.0);
        }else {
            ZSetAdd("group_count", group.getId().toString(), presentCount + 1);
        }

        Double presentOcean = ZsetGetScore("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString());

        if (presentOcean == null) {
            ZSetAdd("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString(), (double) bakku.getCleanWeight());
        } else {
            ZSetAdd("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString(), presentOcean + bakku.getCleanWeight());
        }
    }

    private void updateRanking(BakkuFieldRequest bakkuFieldRequest, Bakku bakku, Group group) {
        Double presentWeight = ZsetGetScore("group_weight", bakku.getGroup().getId().toString());

        ZSetAdd("group_weight", group.getId().toString(), presentWeight - bakku.getCleanWeight() + bakkuFieldRequest.getCleanWeight());

        Double presentOcean = ZsetGetScore("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString());

        ZSetAdd("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString(), presentOcean - bakku.getCleanWeight() + bakkuFieldRequest.getCleanWeight());
    }

    private void deleteRanking(Bakku bakku) {
        Double presentWeight = ZsetGetScore("group_weight", bakku.getGroup().getId().toString());

        ZSetAdd("group_weight"
                , bakku.getGroup().getId().toString()
                , presentWeight - bakku.getCleanWeight());

        Double presentCount = ZsetGetScore("group_count", bakku.getGroup().getId().toString());

        ZSetAdd("group_count"
                , bakku.getGroup().getId().toString()
                , presentCount - 1);

        Double presentOcean = ZsetGetScore("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString());

        ZSetAdd("ocean_" + bakku.getOcean().getId().toString(), bakku.getGroup().getId().toString(),presentOcean - bakku.getCleanWeight());
    }

    private Set<String> getZset(String key, long end) {
        ZSetOperations<String, String> groupRanking = groupStringRedisTemplate.opsForZSet();

        return groupRanking.reverseRange(key, 0, end);
    }

    private Double ZsetGetScore(String key, String value) {
        ZSetOperations<String, String> groupRanking = groupStringRedisTemplate.opsForZSet();

        return groupRanking.score(key, value);
    }

    private void ZSetAdd(String key, String value, Double score) {
        ZSetOperations<String, String> groupRanking = groupStringRedisTemplate.opsForZSet();

        groupRanking.add(key, value, score);
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
