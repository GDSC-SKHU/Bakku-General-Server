package com.gdsc.bakku.storage.service;

import com.gdsc.bakku.storage.domain.entity.Image;
import com.gdsc.bakku.storage.domain.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final GCSService gcsService;
    private final ImageRepository imageRepository;

    /**
     * 이미지를 GCS에 업로드하고 정보를 DB에 저장합니다.
     * @param dirName 서브 디렉토리 이름(뒤에 / 포함 필수)
     * @param multipartFile form-data 형식 파일
     * @return GCS access url
     */
    @Transactional
    public String uploadImage(String dirName, MultipartFile multipartFile) {
        validateImage(multipartFile.getContentType());

        String randomUUIDName = UUID.randomUUID().toString();
        String imageUrl = gcsService.uploadFile(dirName + randomUUIDName, multipartFile);

        Image image = Image.builder()
                .originalName(multipartFile.getOriginalFilename())
                .convertedName(randomUUIDName)
                .imageUrl(imageUrl)
                .build();

        imageRepository.save(image);

        return imageUrl;
    }

    /**
     * 엔티티로 GCS에 있는 이미지를 삭제하고 DB 정보를 삭제합니다.
     * @param dirName 서브 디렉토리 이름(뒤에 / 포함 필수)
     * @param image 이미지 엔티티
     */
    @Transactional
    public void deleteByEntity(String dirName, Image image) {
        gcsService.deleteFile(dirName + image.getConvertedName());
        imageRepository.delete(image);
    }

    /**
     * 파일 형식이 이미지인지 검증하는 메서드
     * @param contentType content-type String 형태
     * @throws ResponseStatusException 이미지가 아니면 400에러 반환
     */
    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            // TODO: 2023/02/16 Custom Exception Handler 구현하기
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 형식은 이미지만 지원합니다.");
        }
    }
}
