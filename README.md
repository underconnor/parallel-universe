# 프로젝트 구성하기

#### API

최상위 계층 인터페이스

---

#### CORE

API의 구현, 실제 실행 코드

---

#### PLUGIN

PaperMC 와 상호작용할 JavaPlugin 을 포함한 코드

* `./gradlew pluginJar` Standalone 플러그인 빌드
* `./gradlew testJar` PUBLISH 프로젝트가 있을때 라이브러리를 참조하는 플러그인 빌드

---

#### PUBLISH `[optional]`

배포용 프로젝트

DONGLE 프로젝트를 사용할 경우 배포되는 CORE#jar가 PUBLISH#coreDongleJar 로 교체

* `./gradlew coreDongleJar` CORE#jar + DONGLE#jar

이 프로젝트를 사용하지 않는다면 삭제해도 됩니다

---