package io.github.m01i0ng.springbootstarteross.core;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.InputStream;
import java.util.List;

public interface OSSTemplate {

  /**
   * 创建 bucket
   *
   * @param bucketName bucket 名字
   */
  void createBucket(String bucketName);

  /**
   * 删除 bucket
   *
   * @param bucketName bucket 名字
   */
  void removeBucket(String bucketName);

  /**
   * 列出所有 bucket
   *
   * @return bucket 列表
   */
  List<Bucket> listAllBuckets();

  /**
   * 上传文件
   *
   * @param bucketName  bucket 名字
   * @param objectName  对象名字
   * @param stream      文件流
   * @param contentType 文件类型
   * @throws Exception 异常
   */
  void putObject(String bucketName, String objectName, InputStream stream, String contentType)
      throws Exception;

  /**
   * 上传文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @param stream     文件流
   * @throws Exception 异常
   */
  void putObject(String bucketName, String objectName, InputStream stream) throws Exception;

  /**
   * 获取文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @return {@link S3Object} 文件对象
   */
  S3Object getObject(String bucketName, String objectName);

  /**
   * 获取文件 url
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @return 文件 url
   */
  String getObjectUrl(String bucketName, String objectName, int expires);

  /**
   * 删除文件
   *
   * @param bucketName bucket 名字
   * @param objectName 对象名字
   * @throws Exception 异常
   */
  void removeObject(String bucketName, String objectName) throws Exception;

  /**
   * 获取文件列表
   *
   * @param bucketName bucket 名字
   * @param prefix     文件前缀
   * @param recursive  是否递归
   * @return 文件描述列表
   */
  List<S3ObjectSummary> listAllObject(String bucketName, String prefix, boolean recursive);
}
