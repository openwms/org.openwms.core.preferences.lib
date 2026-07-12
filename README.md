# Purpose
The OpenWMS.org Preferences Service deals with configuration and preferences for the whole application. It can be used to store
configuration parameters in different validity scopes. Scopes can be merged and inherited. Preferences might be stored only valid for a
particular *User* or a specific *Role*, specific to a *Module* (aka microservice) or the whole *Application*.

This library contains the essential functionality of the Preferences Service and is built with Spring Boot 4.1 on Java 25. It is not a
standalone microservice: it is embedded into the [OpenWMS.org Preferences Service](https://github.com/openwms/org.openwms.core.preferences)
that is distributed as a Docker image.

# Build
A JDK 25 and Maven 3.9+ are required to build. Build the library with all unit and in-memory database integration tests but without a
required [RabbitMQ](https://www.rabbitmq.com) server:

```
./mvnw package
```

To also run the tests against a RabbitMQ instance, running locally with default settings, activate the `AMQP` profile:

```
./mvnw package -DsurefireArgs=-Dspring.profiles.active=AMQP,TEST
```

# Resources

[![Build status](https://github.com/openwms/org.openwms.core.preferences.lib/actions/workflows/master-build.yml/badge.svg)](https://github.com/openwms/org.openwms.core.preferences.lib/actions/workflows/master-build.yml)
[![Quality](https://sonarcloud.io/api/project_badges/measure?project=org.openwms:org.openwms.core.preferences.lib&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.openwms:org.openwms.core.preferences.lib)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Maven central](https://img.shields.io/maven-central/v/org.openwms/org.openwms.core.preferences.lib)](https://search.maven.org/search?q=a:org.openwms.core.preferences.lib)
[![Join the chat at https://gitter.im/openwms/org.openwms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/openwms/org.openwms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**Find further Documentation on [Microservice Website](https://openwms.github.io/org.openwms.core.preferences.lib)** or in the **[Wiki](https://wiki.openwms.cloud/projects/core-preferences-service)**

