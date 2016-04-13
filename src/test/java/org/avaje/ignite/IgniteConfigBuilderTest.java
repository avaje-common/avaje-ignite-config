package org.avaje.ignite;

import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;


public class IgniteConfigBuilderTest {

  private Properties props = new Properties();

  private IgniteConfigBuilder builder;

  IgniteConfigBuilderTest() {
    builder = new IgniteConfigBuilder("ignite", props);
  }

  @Test
  public void setDiscovery() throws Exception {

    props.setProperty("ignite.multicast", "true");
    props.setProperty("ignite.multicast.addresses", "127.0.0.1,127.0.0.2;127.0.0.3");
    builder.setDiscovery();
    assertNotNull(builder.configuration.getDiscoverySpi());
  }

  @Test
  public void parseAddresses() throws Exception {

    Collection<String> addresses = builder.parseAddresses("127.0.0.1, 127.0.0.2;127.0.0.3");
    assertTrue(addresses.contains("127.0.0.1"));
    assertTrue(addresses.contains("127.0.0.2"));
    assertTrue(addresses.contains("127.0.0.3"));
  }

  @Test
  public void setFileSwap_expect_defaultsToNull() throws Exception {

    builder.configuration.setSwapSpaceSpi(null);

    builder.setFileSwap();
    assertNull(builder.configuration.getSwapSpaceSpi());
  }

  @Test
  public void setFileSwap_whenTrue() throws Exception {

    builder.configuration.setSwapSpaceSpi(null);

    props.setProperty("ignite.fileSwap", "true");
    builder.setFileSwap();
    assertNotNull(builder.configuration.getSwapSpaceSpi());
  }

  @Test
  public void testGetBool() throws Exception {

    props.setProperty("ignite.something", "TRUE");
    assertTrue(builder.getBool("something.else", true));
    assertFalse(builder.getBool("something.else", false));
    assertTrue(builder.getBool("something", false));

    props.setProperty("ignite.something", "false");
    assertFalse(builder.getBool("something", false));

    props.clear();
  }

  @Test
  public void testGetInt() throws Exception {

    props.setProperty("ignite.something", "42");
    assertEquals(builder.getInt("something", 12), 42);
    assertEquals(builder.getInt("something.else", 12), 12);
    props.clear();
  }

  @Test
  public void testGet() throws Exception {

    props.setProperty("ignite.something", "Foo");
    assertEquals(builder.get("something", "junk"), "Foo");
    assertEquals(builder.get("something.notExisting", "junk"), "junk");

    props.clear();
  }

  @Test
  public void testGet_caseInsensitive_whenMixed() throws Exception {

    props.setProperty("ignite.someFooBar", "Foo");
    assertEquals(builder.get("someFooBar", "junk"), "Foo");
    assertEquals(builder.get("somefoobar", "junk"), "junk");

    props.clear();
  }

  @Test
  public void testGet_caseInsensitive_whenLower() throws Exception {

    props.setProperty("ignite.somefoobar", "Foo");
    assertEquals(builder.get("someFooBar", "junk"), "Foo");
    assertEquals(builder.get("somefoobar", "junk"), "Foo");

    props.clear();
  }

}