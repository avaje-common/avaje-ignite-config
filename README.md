# avaje-ignite-config
Builds an IgniteConfiguration from properties (can be used as simple configuration mechanism for both servers and clients)

## Example use

```java

    // get Properties from somewhere
    Properties properties = PropertiesLoader.load();
    
    // give IgniteConfigBuilder properties and an optional prefix
    // ... and build a IgniteConfiguration 
    IgniteConfiguration igniteConfiguration = new IgniteConfigBuilder("ignite", properties).build();
    
    // now use the configuration to start a server
    // ... in this case IgniteServer is part of avaje-ignite-server 
    IgniteServer server = new IgniteServer(true, igniteConfiguration);
    server.run();
    
```
