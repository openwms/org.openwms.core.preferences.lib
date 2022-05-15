## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

|Parameter|Type|Default profile value|Description|
|---------|----|-----------|
|owms.eureka.url|string|http://user:sa@localhost:8761|The base URL of the running Eureka service discovery server, inclusive schema and port|
|owms.eureka.zone|string|http://user:sa@localhost:8761/eureka/|The full Eureka registration endpoint URL|
|owms.service.protocol|string|http|The protocol the service' is accessible from Eureka clients|  
|owms.service.hostname|string|localhost|The hostname the service' is accessible from Eureka clients|
|owms.core.config.initial-properties|string|classpath:initial-preferences.xml|A Spring resource path to a XML file that contains all initial properties that shall be loaded into the database at startup|
