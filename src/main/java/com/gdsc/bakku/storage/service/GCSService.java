package com.gdsc.bakku.storage.service;

import com.gdsc.bakku.common.exception.CouldNotGenerateInputStreamException;
import com.gdsc.bakku.common.exception.GCSFIleNotFoundException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GCSService {

    private final Storage storage;

    @Value("${bucket.name}")
    private String bucketName;

    /**
     * GCS에 파일을 업로드합니다.
     * @param filePath 파일명
     * @param multipartFile form-data 형식의 파일
     * @return 접근 가능한 url
     * @throws ResponseStatusException 입력 스트림에서 IOException 이 일어날 때 500 반환
     */
    public String uploadFile(String filePath, MultipartFile multipartFile) {
        BlobInfo blobInfo;
        try {
            blobInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, filePath)
                            .setContentType(multipartFile.getContentType())
                            .build(),
                    multipartFile.getInputStream()
            );
        } catch (IOException e) {
            throw new CouldNotGenerateInputStreamException();
        }

        return blobInfo.getMediaLink();
    }

    /**
     * GCS에서 파일명으로 Blob을 찾아 존재하면 삭제합니다.
     * @param filePath 파일명
     * @throws ResponseStatusException GCS에 해당 파일명이 존재하지 않을 때 404 반환
     */
    public void deleteFile(String filePath) {
        boolean isDelete = storage.delete(BlobId.of(bucketName, filePath));

        if (!isDelete) {
            throw new GCSFIleNotFoundException();
        }
    }
}
