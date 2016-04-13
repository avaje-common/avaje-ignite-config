package org.avaje.ignite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load the properties from file system.
 */
class PropertiesLoad {

  /**
   * Load the properties file.
   */
  public static Properties load(String resource) {

    Properties props = new Properties();
    try {
      InputStream is = PropertiesLoad.class.getResourceAsStream(resource);
      props.load(is);
      is.close();
      return props;

    } catch (IOException e) {
      throw new RuntimeException("Error loading properties file "+resource, e);
    }
  }
}
