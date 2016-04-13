package org.avaje.ignite;

import com.amazonaws.auth.BasicAWSCredentials;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder;

/**
 * Build TcpDiscoveryIpFinder using AWS specific TcpDiscoveryS3IpFinder.
 */
class AwsDiscoveryBuilder {

  private final String bucket;
  private final String accessKey;
  private final String secretKey;
  private final String shared;

  AwsDiscoveryBuilder(String bucket, String accessKey, String secretKey, String shared) {

    this.bucket = bucket;
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.shared = shared;
  }

  TcpDiscoveryIpFinder build() {

    TcpDiscoveryS3IpFinder s3IpFinder = new TcpDiscoveryS3IpFinder();
    s3IpFinder.setBucketName(bucket);
    s3IpFinder.setAwsCredentials(new BasicAWSCredentials(accessKey, secretKey));

    if (shared != null && !shared.isEmpty()) {
      s3IpFinder.setShared(Boolean.parseBoolean(shared));
    }
    //s3IpFinder.setClientConfiguration();

    return s3IpFinder;
  }
}
