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

public class IgniteConfigBuilderStaticTest {

  @Test
  public void load() {

    Properties props = PropertiesLoad.load("/example-staticip.properties");

    IgniteConfigBuilder builder = new IgniteConfigBuilder("ignite", props);
    IgniteConfiguration config = builder.build();

    TcpDiscoverySpi discoverySpi = (TcpDiscoverySpi)config.getDiscoverySpi();

    //assertEquals(discoverySpi.getLocalPort(), 48501);
    assertEquals(discoverySpi.getLocalPortRange(), 21);

    TcpDiscoveryVmIpFinder ipFinder = (TcpDiscoveryVmIpFinder)discoverySpi.getIpFinder();
    Collection<InetSocketAddress> addresses = ipFinder.getRegisteredAddresses();

    assertTrue(!addresses.isEmpty());

  }
}