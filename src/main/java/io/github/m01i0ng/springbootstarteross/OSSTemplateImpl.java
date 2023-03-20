package io.github.m01i0ng.springbootstarteross;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import io.github.m01i0ng.springbootstarteross.core.OSSTemplate;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class OSSTemplateImpl implements OSSTemplate {

  private final AmazonS3 amazonS3;

  /**
   * 创建 bucket
   *
   * @param bucketName bucket 名字
   */
  @Override
  public void createBucket(String bucketName) {
    if (!amazonS3.doesBucketExistV2(bucketName)) {
      amazonS3.createBucket(bucketName);
    }
  }

  /**
   * 删除 bucket
   *
   * @param bucketName bucket 名字
   */
  @Override
  public void removeBucket(String bucketName) {
    if (amazonS3.doesBucketExistV2(bucketName)) {
      amazonS3.deleteBucket(bucketName);
    }
  }

  /**
   * 列出所有 bucket
   *
   * @return bucket 列表
   */
  @Override
  public List<Bucket> listAllBuckets() {
    return amazonS3.listBuckets();
  }

  @SneakyThrows
  private PutObjectResult putObject(String bucketName, String objectName, InputStream stream,
      long size, String contentType) {
    byte[] bytes = IOUtils.toByteArray(stream);
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(contentType);
    objectMetadata.setContentLength(size);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
    return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
  }

  /**
   * 上传文件
   *
   * @param bucketName  bucket 名字
   * @param objectName  对象名字
   * @param stream      文件流
   * @param contentType 文件类型
   * @throws Exception 异常
   */
  @Override
  public void putObject(String bucketName, String objectName, InputStream stream,
      String contentType) throws Exception {
    putObject(bucketName, objectName, stream, stream.available(), contentType);
  }

  /**
   * 上传文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @param stream     文件流
   * @throws Exception 异常
   */
  @Override
  public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
    String contentType = URLConnection.guessContentTypeFromStream(stream);
    if (contentType != null) {
      contentType = "application/octet-stream";
    }

    putObject(bucketName, objectName, stream, contentType);
  }

  /**
   * 获取文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @return 文件对象
   */
  @Override
  public S3Object getObject(String bucketName, String objectName) {
    return amazonS3.getObject(bucketName, objectName);
  }

  /**
   * 获取文件 url
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @return 文件 url
   */
  @Override
  public String getObjectUrl(String bucketName, String objectName, int expires) {
    Date date = new Date();
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, expires);
    URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
    return url.toString();
  }

  /**
   * 删除文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @throws Exception 异常
   */
  @Override
  public void removeObject(String bucketName, String objectName) throws Exception {
    amazonS3.deleteObject(bucketName, objectName);
  }

  /**
   * 获取文件列表
   *
   * @param bucketName bucket 名字
   * @param prefix     文件前缀
   * @param recursive  是否递归
   * @return 文件描述列表
   */
  @Override
  public List<S3ObjectSummary> listAllObject(String bucketName, String prefix, boolean recursive) {
    ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
    return objectListing.getObjectSummaries();
  }
}
