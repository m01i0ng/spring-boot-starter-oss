package io.github.m01i0ng.springbootstarteross;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import io.github.m01i0ng.springbootstarteross.core.OSSProperties;
import io.github.m01i0ng.springbootstarteross.core.OSSTemplate;
import io.github.m01i0ng.springbootstarteross.server.OSSTemplateImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OSSProperties.class)
@RequiredArgsConstructor
public class OSSAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public AmazonS3 ossClient(OSSProperties properties) {
    ClientConfiguration clientConfiguration = new ClientConfiguration();
    clientConfiguration.setMaxConnections(properties.getMaxConnections());
    EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
        properties.getEndpoint(), properties.getRegion());
    BasicAWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(),
        properties.getSecretKey());
    AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
        credentials);
    return AmazonS3Client.builder()
        .withEndpointConfiguration(endpointConfiguration)
        .withClientConfiguration(clientConfiguration)
        .withCredentials(credentialsProvider)
        .withPathStyleAccessEnabled(properties.isPathStyleAccess())
        .disableChunkedEncoding()
        .build();
  }

  @Bean
  @ConditionalOnBean(AmazonS3.class)
  public OSSTemplate ossTemplate(AmazonS3 amazonS3) {
    return new OSSTemplateImpl(amazonS3);
  }
}
