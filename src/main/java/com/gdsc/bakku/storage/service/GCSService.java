package com.gdsc.bakku.storage.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class GCSService {

    private final Storage storage;

    @Value("${bucket.name}")
    private String bucketName;

    /**
     * GCS에 파일을 업로드합니다.
     * @param fileName 파일명
     * @param contentType 파일 확장자
     * @param inputStream 파일 입력 스트림
     * @return 접근 가능한 url
     * @throws ResponseStatusException 입력 스트림에서 IOException 이 일어날 때 500 반환
     */
    public String uploadFile(String fileName, String contentType, InputStream inputStream) {
        BlobInfo blobInfo;
        try {
            blobInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, fileName)
                            .setContentType(contentType)
                            .build(),
                    inputStream
            );
        } catch (IOException e) {
            // TODO: 2023/02/16 Custom Exception Handler 구현하기
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "입력 스트림을 생성하는 과정에서 문제가 생겼습니다.");
        }

        return blobInfo.getMediaLink();
    }

    /**
     * GCS에서 파일명으로 Blob을 찾아 존재하면 삭제합니다.
     * @param fileName 파일명
     * @throws ResponseStatusException GCS에 해당 파일명이 존재하지 않을 때 404 반환
     */
    public void deleteFile(String fileName) {
        boolean isDelete = storage.delete(BlobId.of(bucketName, fileName));

        if (!isDelete) {
            // TODO: 2023/02/16 Custom Exception Handler 구현하기
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("GCS에 해당 파일명(%s)이 존재하지 않습니다.", fileName));
        }
    }
}
