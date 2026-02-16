package com.hengyu.lab.framework.oss;

import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OssTemplate {

  private final MinioClient minioClient;
  private final MinioConfig minioConfig;


  public String uploadFile(Long id, InputStream inputStream, String originalFilename) {
    try {
      boolean found = minioClient.bucketExists(
          BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
      if (!found) {
        minioClient.makeBucket(
            MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
      }

      String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
      String key = "/papers" + id + suffix;
      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(minioConfig.getBucketName())
              .object(key)
              .stream(inputStream, inputStream.available(), -1)
              .build()
      );
      return key;
    } catch (Exception e) {
      log.error("MinIO 上传失败", e);
      throw new BizException(ResultCode.UPLOAD_FILE_FAILE, e);
    }
  }


  public String getPresignedUrl(String key) {
    try {
      Map<String, String> reqParams = new HashMap<>();
      reqParams.put("response-content-disposition", "attachment; filename=\"paper.pdf\"");
      return minioClient.getPresignedObjectUrl(
          GetPresignedObjectUrlArgs.builder()
              .method(Method.GET)
              .bucket(minioConfig.getBucketName())
              .object(key)
              .expiry(7, TimeUnit.DAYS)
              .extraQueryParams(reqParams)
              .build()
      );
    } catch (Exception e) {
      log.error("获取文件连接失败", e);
      throw new BizException(ResultCode.GET_FILE_URL_FAILE, e);
    }
  }

  public void remove(String key) {
    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(minioConfig.getBucketName())
              .object(key)
              .build()
      );
    } catch (Exception e) {
      log.error("删除文件失败", e);
      throw new BizException(ResultCode.REMOVE_FILE_FAILE, e);
    }
  }
}
