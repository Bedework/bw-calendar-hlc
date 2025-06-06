# bw-calendar-hlc [![Build Status](https://travis-ci.org/Bedework/bw-calendar-engine.svg)](https://travis-ci.org/Bedework/bw-calendar-engine)
High-level clients for [Bedework](https://www.apereo.org/projects/bedework).

This project is still in development.

## Requirements

1. JDK 21
2. Maven 3

## Building Locally

> mvn clean install

## Releasing

Releases of this project are published to Maven Central via Sonatype.

To create a release, you must have:

1. Permissions to publish to the `org.bedework` groupId.
2. `gpg` installed with a published key (release artifacts are signed).

To perform a new release:

> mvn -P bedework-dev release:clean release:prepare

When prompted, select the desired version; accept the defaults for scm tag and next development version.
When the build completes, and the changes are committed and pushed successfully, execute:

> mvn -P bedework-dev release:perform

For full details, see [Sonatype's documentation for using Maven to publish releases](http://central.sonatype.org/pages/apache-maven.html).

## Release Notes
