package org.avaje.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class IgniteConfigBuilderTcpTest {

  @Test
  public void load() {

    Properties props = PropertiesLoad.load("/test-tcp.properties");

    IgniteConfigBuilder builder = new IgniteConfigBuilder(null, props);
    IgniteConfiguration config = builder.build();

    TcpDiscoverySpi discoverySpi = (TcpDiscoverySpi)config.getDiscoverySpi();

    //assertEquals(discoverySpi.getLocalPort(), 48501);
    assertEquals(discoverySpi.getLocalPortRange(), 11);
    assertEquals(discoverySpi.getLocalAddress(), "hello");

    assertEquals(discoverySpi.getReconnectCount(), 42);
    assertEquals(discoverySpi.getAckTimeout(), 43);
    assertEquals(discoverySpi.getSocketTimeout(), 44);
    assertEquals(discoverySpi.getNetworkTimeout(), 45);
    assertEquals(discoverySpi.getJoinTimeout(), 46);
    assertEquals(discoverySpi.getHeartbeatFrequency(), 47);
    assertEquals(discoverySpi.getStatisticsPrintFrequency(), 48);
    assertEquals(discoverySpi.getMaxAckTimeout(), 49);
    assertEquals(discoverySpi.getMaxMissedClientHeartbeats(), 50);
    assertEquals(discoverySpi.getMaxMissedHeartbeats(), 51);

  }
}