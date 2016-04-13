package org.avaje.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class IgniteConfigBuilderAwsTest {

  @Test
  public void load() {

    Properties props = PropertiesLoad.load("/example-aws.properties");

    IgniteConfigBuilder builder = new IgniteConfigBuilder("ignite", props);
    IgniteConfiguration config = builder.build();

    TcpDiscoverySpi discoverySpi = (TcpDiscoverySpi)config.getDiscoverySpi();
    assertEquals(discoverySpi.getLocalPortRange(), 11);

    TcpDiscoveryIpFinder ipFinder = discoverySpi.getIpFinder();
    assertTrue(ipFinder instanceof TcpDiscoveryS3IpFinder);
  }
}