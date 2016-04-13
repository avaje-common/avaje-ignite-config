package org.avaje.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class IgniteConfigBuilderCommunicationTest {

  @Test
  public void load() {

    Properties props = PropertiesLoad.load("/test-communication.properties");

    IgniteConfigBuilder builder = new IgniteConfigBuilder(null, props);
    IgniteConfiguration config = builder.build();

    TcpCommunicationSpi communication = (TcpCommunicationSpi)config.getCommunicationSpi();

    assertEquals(communication.getLocalAddress(), "hello");
    assertEquals(communication.getLocalPort(), 42);
    assertEquals(communication.getConnectTimeout(), 43);
    assertEquals(communication.getIdleConnectionTimeout(), 44);
    assertEquals(communication.getLocalPortRange(), 45);
    assertEquals(communication.getMaxConnectTimeout(), 46);
    assertEquals(communication.getSocketReceiveBuffer(), 47);
    assertEquals(communication.getSocketSendBuffer(), 48);
    assertEquals(communication.getSocketWriteTimeout(), 49);
    assertEquals(communication.isDirectBuffer(), true);
    assertEquals(communication.isDirectSendBuffer(), true);
    assertEquals(communication.getAckSendThreshold(), 52);
    assertEquals(communication.getMessageQueueLimit(), 53);
    assertEquals(communication.getReconnectCount(), 54);
    assertEquals(communication.getSelectorsCount(), 55);
    assertEquals(communication.getSlowClientQueueLimit(), 56);
    assertEquals(communication.getUnacknowledgedMessagesBufferSize(), 57);
    assertEquals(communication.isTcpNoDelay(), true);
    assertEquals(communication.getName(), "foo");

  }
}