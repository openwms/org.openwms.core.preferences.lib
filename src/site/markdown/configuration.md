## Configuration
OpenWMS.org defines additional configuration parameters beside the standard Spring Framework ones. All custom parameters are children of the
`owms` property namespace.

| Parameter                           | Type   | Default profile value               | Description                                                                                                                 |
|-------------------------------------|--------|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| DB_DRIVER_CLASS                     | string | `org.postgresql.Driver`             | FQN of the JDBC driver class                                                                                                |
| DB_URL                              | string | `jdbc:postgresql://db/demodb`       | JDBC connection URL                                                                                                         |
| DB_USERNAME                         | string | `demo`                              | JDBC connection username                                                                                                    |
| DB_PASSWORD                         | string | `DEMO`                              | JDBC connection password                                                                                                    |
| DB_DIALECT                          | string | `postgresql`                        | JDBC dialect                                                                                                                |
| DB_PASSWORD                         | string | `DEMO`                              | JDBC connection password                                                                                                    |
| owms.eureka.hostname                | string | `localhost`                         | Hostname where the discovery server is running                                                                              |
| owms.eureka.port                    | string | `8761`                              | Port of the discovery server instance                                                                                       |
| owms.eureka.url                     | string | `http://user:sa@localhost:8761`     | URI to connect to the discovery server - a combination or hostname, port, username and password                             |
| owms.eureka.user.name               | string | `user`                              | User's name to access the discovery server                                                                                  |
| owms.eureka.user.password           | string | `sa`                                | User's password to access to the discovery server                                                                           |
| owms.eureka.zone                    | string | `${owms.eureka.url}/eureka/`        | URI to get the zone settings from Eureka discovery server                                                                   |
| owms.srv.hostname                   | string | `localhost`                         | The hostname the service' is accessible from Eureka clients                                                                 |
| owms.srv.protocol                   | string | `http`                              | The protocol the service' is accessible from Eureka clients                                                                 |
| owms.core.config.initial-properties | string | `classpath:initial-preferences.xml` | A Spring resource path to a XML file that contains all initial properties that shall be loaded into the database at startup |
