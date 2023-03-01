# Packman

[![Build](https://github.com/TonnyL/Packman/workflows/build/badge.svg)](https://github.com/TonnyL/Packman/actions?query=workflow%3Abuild)

A mobile application made for managing GitLab CI/CD jobs, demonstrating how to implement some latest technologies in mobile software development.

<p align="center">
<img src="https://user-images.githubusercontent.com/13329148/222216780-d851cc33-3eaf-4c57-90bd-603b6e521f32.jpg" height="500"/>
<img src="https://user-images.githubusercontent.com/13329148/222216998-9646f0d0-bac0-4e5f-bd5d-885acb16c5d4.jpg" height="500"/>
</p>

## Feature
+ 100% Kotlin;
+ The whole UI is written with [Compose Multiplatform](https://github.com/JetBrains/compose-jb/);
+ The business logic is shared between Android and iOS (with [Kotlin Multiplatform (Mobile)](https://kotlinlang.org/docs/multiplatform.html)).

## Build Instructions

### Prerequisites
+ Android Studio Electric Eel 2022.1.1 Patch 1 or higher;
+ Xcode 14.2 or higher(if you want to build iOS app);
+ JDK 17 or higher;
+ Kotlin 1.8.0 or higher;
+ [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) installed in Android Studio(optional).

### API Keys and Configurations
Put them into `local.properties` file:

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

### Build
```shell
./gradlew build
```

## License
Packman is under an MIT license. See the [LICENSE](LICENSE) file for more information.
