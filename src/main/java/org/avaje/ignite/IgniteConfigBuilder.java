package org.avaje.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.swapspace.file.FileSwapSpaceSpi;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Builds Ignite configuration from properties.
 */
public class IgniteConfigBuilder {

  private final String prefix;

  private final Properties properties;

  final IgniteConfiguration configuration = new IgniteConfiguration();

  /**
   * Construct with an optional prefix and properties.
   * <p>
   * Often the prefix will be "ignite" or similar to provide a name space in the properties.
   * </p>
   */
  public IgniteConfigBuilder(String prefix, Properties properties) {
    this.prefix = prefix;
    this.properties = properties;
  }

  /**
   * Build and return the IgniteConfiguration from the supplied properties.
   */
  public IgniteConfiguration build() {

    configuration.setClientMode(getBool("clientMode", false));

    setFileSwap();
    setDiscovery();
    setCommunication();

    return configuration;
  }

  protected void setCommunication() {

    if (!getBool("communication", false)) {
      return;
    }

    TcpCommunicationSpi communication = new TcpCommunicationSpi();

    int localPort = getInt("communication.localPort", -1);
    if (localPort > -1) {
      communication.setLocalPort(localPort);
    }
    String address = get("communication.localAddress","");
    if (!address.isEmpty()) {
      communication.setLocalAddress(address);
    }

    int timeout = getInt("communication.connectTimeout", -1);
    if (timeout > -1) {
      communication.setConnectTimeout(timeout);
    }
    int idleTimeout = getInt("communication.idleConnectTimeout", -1);
    if (idleTimeout > -1) {
      communication.setIdleConnectionTimeout(idleTimeout);
    }
    int range = getInt("communication.localPortRange", -1);
    if (range > -1) {
      communication.setLocalPortRange(range);
    }
    int maxTimeout = getInt("communication.maxConnectTimeout", -1);
    if (maxTimeout > -1) {
      communication.setMaxConnectTimeout(maxTimeout);
    }
    int socketReceiveBuffer = getInt("communication.socketReceiveBuffer", -1);
    if (socketReceiveBuffer > -1) {
      communication.setSocketReceiveBuffer(socketReceiveBuffer);
    }
    int socketSendBuffer = getInt("communication.socketSendBuffer", -1);
    if (socketSendBuffer > -1) {
      communication.setSocketSendBuffer(socketSendBuffer);
    }
    int socketWriteTimeout = getInt("communication.socketWriteTimeout", -1);
    if (socketWriteTimeout > -1) {
      communication.setSocketWriteTimeout(socketWriteTimeout);
    }
    String directBuffer = get("communication.directBuffer","");
    if (!directBuffer.isEmpty()) {
      communication.setDirectBuffer(Boolean.parseBoolean(directBuffer));
    }
    String directSendBuffer = get("communication.directSendBuffer","");
    if (!directSendBuffer.isEmpty()) {
      communication.setDirectSendBuffer(Boolean.parseBoolean(directSendBuffer));
    }
    int ackSendThreshold = getInt("communication.ackSendThreshold", -1);
    if (ackSendThreshold > -1) {
      communication.setAckSendThreshold(ackSendThreshold);
    }
    int messageQueueLimit = getInt("communication.messageQueueLimit", -1);
    if (messageQueueLimit > -1) {
      communication.setMessageQueueLimit(messageQueueLimit);
    }
    int reconnectCount = getInt("communication.reconnectCount", -1);
    if (reconnectCount > -1) {
      communication.setReconnectCount(reconnectCount);
    }
    int selectorsCount = getInt("communication.selectorsCount", -1);
    if (selectorsCount > -1) {
      communication.setSelectorsCount(selectorsCount);
    }
    int slowClientQueueLimit = getInt("communication.slowClientQueueLimit", -1);
    if (slowClientQueueLimit > -1) {
      communication.setSlowClientQueueLimit(slowClientQueueLimit);
    }
    int unacknowledgedMessagesBufferSize = getInt("communication.unacknowledgedMessagesBufferSize", -1);
    if (unacknowledgedMessagesBufferSize > -1) {
      communication.setUnacknowledgedMessagesBufferSize(unacknowledgedMessagesBufferSize);
    }
    String tcpNoDelay = get("communication.tcpNoDelay","");
    if (!tcpNoDelay.isEmpty()) {
      communication.setTcpNoDelay(Boolean.parseBoolean(tcpNoDelay));
    }
    String name = get("communication.name","");
    if (!name.isEmpty()) {
      communication.setName(name);
    }

    configuration.setCommunicationSpi(communication);
  }

  protected void setDiscovery() {
    DiscoverySpi discovery = buildDiscovery();
    if (discovery != null) {
      configuration.setDiscoverySpi(discovery);
    }
  }

  protected DiscoverySpi buildDiscovery() {

    if (get("aws.accessKey", null) != null) {
      return discoveryAws();
    }

    if (get("staticip.addresses", null) != null) {
      return discoveryStaticIp();
    }

    // default to use multicast
    return discoveryMulticast();
  }

  protected DiscoverySpi discoveryAws() {

    String bucket = get("aws.bucketName", "");
    String accessKey = get("aws.accessKey", null);
    String secretKey = get("aws.secretKey", null);
    String shared = get("aws.shared", "");

    TcpDiscoverySpi discovery = buildTcpDiscovery();
    discovery.setIpFinder(new AwsDiscoveryBuilder(bucket, accessKey, secretKey, shared).build());

    return discovery;
  }

  protected DiscoverySpi discoveryMulticast() {

    TcpDiscoveryMulticastIpFinder multiCast = new TcpDiscoveryMulticastIpFinder();

    String addresses = get("multicast.addresses", "");
    if (!addresses.isEmpty()) {
      multiCast.setAddresses(parseAddresses(addresses));
    }
    String localAddress = get("multicast.localAddress", "");
    if (!localAddress.isEmpty()) {
      multiCast.setLocalAddress(localAddress);
    }
    String group = get("multicast.multicastGroup", "");
    if (!group.isEmpty()) {
      multiCast.setMulticastGroup(group);
    }
    int port = getInt("multicast.multicastPort", -1);
    if (port > -1) {
      multiCast.setMulticastPort(port);
    }
    int attempts = getInt("multicast.addressRequestAttempts", -1);
    if (attempts > -1) {
      multiCast.setAddressRequestAttempts(attempts);
    }
    int waitTime = getInt("multicast.responseWaitTime", -1);
    if (waitTime > -1) {
      multiCast.setResponseWaitTime(waitTime);
    }
    int ttl = getInt("multicast.timeToLive", -1);
    if (ttl > -1) {
      multiCast.setTimeToLive(ttl);
    }
    String shared = get("multicast.shared","");
    if (!shared.isEmpty()) {
      multiCast.setShared(Boolean.parseBoolean(shared));
    }

    TcpDiscoverySpi discovery = buildTcpDiscovery();
    discovery.setIpFinder(multiCast);

    return discovery;
  }


  /**
   * Return static IP address based discovery.
   */
  protected DiscoverySpi discoveryStaticIp() {

    String addresses = get("staticip.addresses", "");
    if (addresses.isEmpty()) {
      throw new IllegalStateException("No staticip.addresses defined?");
    }

    TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
    ipFinder.setAddresses(parseAddresses(addresses));

    TcpDiscoverySpi discovery = buildTcpDiscovery();
    discovery.setIpFinder(ipFinder);
    return  discovery;
  }

  protected TcpDiscoverySpi buildTcpDiscovery() {

    TcpDiscoverySpi discovery = new TcpDiscoverySpi();

    int localPort = getInt("tcp.localPort", -1);
    if (localPort > -1) {
      discovery.setLocalPort(localPort);
    }
    int localPortRange = getInt("tcp.localPortRange", -1);
    if (localPortRange > -1) {
      discovery.setLocalPortRange(localPortRange);
    }
    String address = get("tcp.localAddress","");
    if (!address.isEmpty()) {
      discovery.setLocalAddress(address);
    }
    int reconnectCount = getInt("tcp.reconnectCount", -1);
    if (reconnectCount > -1) {
      discovery.setReconnectCount(reconnectCount);
    }
    int ackTimeout = getInt("tcp.ackTimeout", -1);
    if (ackTimeout > -1) {
      discovery.setAckTimeout(ackTimeout);
    }
    int socketTimeout = getInt("tcp.socketTimeout", -1);
    if (socketTimeout > -1) {
      discovery.setSocketTimeout(socketTimeout);
    }
    int networkTimeout = getInt("tcp.networkTimeout", -1);
    if (networkTimeout > -1) {
      discovery.setNetworkTimeout(networkTimeout);
    }
    int joinTimeout = getInt("tcp.joinTimeout", -1);
    if (joinTimeout > -1) {
      discovery.setJoinTimeout(joinTimeout);
    }
    int heartbeatFrequency = getInt("tcp.heartbeatFrequency", -1);
    if (heartbeatFrequency > -1) {
      discovery.setHeartbeatFrequency(heartbeatFrequency);
    }
    int statisticsPrintFrequency = getInt("tcp.statisticsPrintFrequency", -1);
    if (statisticsPrintFrequency > -1) {
      discovery.setStatisticsPrintFrequency(statisticsPrintFrequency);
    }
    int maxAckTimeout = getInt("tcp.maxAckTimeout", -1);
    if (maxAckTimeout > -1) {
      discovery.setMaxAckTimeout(maxAckTimeout);
    }
    int maxMissedClientHeartbeats = getInt("tcp.maxMissedClientHeartbeats", -1);
    if (maxMissedClientHeartbeats > -1) {
      discovery.setMaxMissedClientHeartbeats(maxMissedClientHeartbeats);
    }
    int maxMissedHeartbeats = getInt("tcp.maxMissedHeartbeats", -1);
    if (maxMissedHeartbeats > -1) {
      discovery.setMaxMissedHeartbeats(maxMissedHeartbeats);
    }

    return discovery;
  }

  /**
   * Set FileSwapSpaceSpi if set.
   */
  protected void setFileSwap() {
    if (getBool("fileSwap", false)) {
      configuration.setSwapSpaceSpi(new FileSwapSpaceSpi());
    }
  }

  protected Set<String> parseAddresses(String delimitedAddresses) {

    String[] split = delimitedAddresses.split("[,;]");

    Set<String> addresses = new LinkedHashSet<>();
    for (String address : split) {
      addresses.add(address.trim());
    }
    return addresses;
  }

  /**
   * Return an int property.
   */
  protected int getInt(String key, int defaultValue) {
    String val = get(key, Integer.toString(defaultValue));
    return Integer.valueOf(val);
  }

  /**
   * Return a boolean property.
   */
  protected boolean getBool(String key, boolean defaultValue) {

    String val = get(key, Boolean.toString(defaultValue));
    return Boolean.valueOf(val.toLowerCase());
  }

  /**
   * Return a string property.
   */
  protected String get(String key, String defaultValue) {

    String fullKey = prefixKey(key);
    String value = System.getProperty(fullKey);
    if (value != null) {
      return value;
    }

    value = properties.getProperty(fullKey);
    if (value == null) {
      // maybe the properties are loaded all lower case
      value = properties.getProperty(fullKey.toLowerCase());
    }
    return trim((value != null) ? value : defaultValue);
  }

  protected String prefixKey(String key) {
    return (prefix == null) ? key : prefix + "." + key;
  }

  protected String trim(String value) {
    return (value == null) ? null : value.trim();
  }
}
