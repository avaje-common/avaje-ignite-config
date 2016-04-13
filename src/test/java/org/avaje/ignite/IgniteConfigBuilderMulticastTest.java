package org.avaje.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class IgniteConfigBuilderMulticastTest {

  @Test
  public void load() {

    Properties props = PropertiesLoad.load("/example-multicast.properties");

    IgniteConfigBuilder builder = new IgniteConfigBuilder("ignite", props);
    IgniteConfiguration config = builder.build();

    TcpDiscoverySpi discoverySpi = (TcpDiscoverySpi)config.getDiscoverySpi();
    assertEquals(discoverySpi.getLocalPortRange(), 11);

    TcpDiscoveryMulticastIpFinder ipFinder = (TcpDiscoveryMulticastIpFinder)discoverySpi.getIpFinder();
    Collection<InetSocketAddress> addresses = ipFinder.getRegisteredAddresses();

    assertEquals(ipFinder.getMulticastGroup(), "228.10.10.157");
    assertTrue(!addresses.isEmpty());
  }
}