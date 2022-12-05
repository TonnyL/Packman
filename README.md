# Packman

[![Build](https://github.com/TonnyL/Packman/workflows/build/badge.svg)](https://github.com/TonnyL/Packman/actions?query=workflow%3Abuild)

An Android app for managing GitLab CI/CD jobs.

Put your configurations into `local.properties` file:

```java
ACCESS_TOKEN=Your access token
TRIGGER_PIPELINE_ACCESS_TOKEN=Your project level token
PROJECT_PATH=Path of your project
PROJECT_ID=Your project id
GRAPH_QL_SERVER_URL=https://your.gitlab.server.address/api/graphql
REST_SERVER_URL=https://your.gitlab.server.address/api/v4

STORE_FILE_PATH=Path to store file
STORE_PASSWORD=Password
KEY_ALIAS=Key alias
KEY_PASSWORD=Key password
```
