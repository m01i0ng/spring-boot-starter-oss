package io.github.m01i0ng.springbootstarteross.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author m01i0ng
 */
@Data
@ConfigurationProperties(prefix = "spring.storage.oss")
public class OSSProperties {

  /**
   * 对象存储服务的 url
   */
  private String endpoint;

  /**
   * 区域
   */
  private String region;

  /**
   * 是否支持 pathStyle 模式
   */
  private boolean pathStyleAccess = true;

  /**
   * Access Key
   */
  private String accessKey;

  /**
   * Secret Key
   */
  private String secretKey;

  /**
   * 最大线程数
   */
  private Integer maxConnections = 10;
}
