# 숨 쉴 틈 — Breath Space

> **바쁜 일상 속에서 온전히 나를 위해 숨을 고르는 고요한 공간**

Breath Space는 복잡한 현대 사회 속에서 잠시 속도를 늦추고, 당신만의 고유한 호흡 템포를 회복할 수 있도록 디자인된 Android 마음 챙김 및 심호흡 애플리케이션입니다.

Breath Space is a local-first, offline-first Android mindfulness app designed to restore inner peace through slow, immersive glassmorphic animations, customized rest timing, and offline mood logs.

---

<p align="center">
  <img src="docs/assets/key-visual.png" alt="Breath Space Key Visual" width="700">
</p>

---

## 🧘 핵심 가치 & 브랜딩

- **정적인 몰입감 (Atmospheric Calm)**: 부드럽고 유려한 그라데이션 구체를 마주하며 자연스럽게 숨을 모으고 비워내도록 돕습니다.
- **로컬 우선 정책 (Offline-First)**: 사용자의 감정 상태 기록이나 세션 통계 데이터는 외부 서버로 송출되지 않으며, 디바이스의 앱 전용 로컬 저장소에만 보관됩니다. 클라우드 백업도 비활성화되어 있습니다.
- **일상과의 연결 (Gentle Reminders)**: 하루 중 마음에 휴식이 꼭 필요한 최적의 주기적 순간을 알림 설정으로 연결합니다.

---

## 🛠️ 기술 스택

| 영역 | 선택 기술 |
|---|---|
| **언어 (Language)** | Kotlin |
| **UI 프레임워크** | Jetpack Compose, Material 3 |
| **아키텍처 (Architecture)** | MVVM 아키텍처 |
| **로컬 데이터베이스** | Room Database |
| **비동기 처리** | Kotlin Coroutines & Flow |
| **빌드 구성** | Gradle Kotlin DSL + 버전 카탈로그 (`libs.versions.toml`) |
| **의존성 주입** | 수동 DI (Manual DI) |

---

## 📦 저장소 구조

```text
breath-space/
  app/
    src/main/
      java/com/jeiel85/breathspace/
        data/         # Room Database 및 리포지토리 레이어
        receiver/     # 심호흡 리마인더 스케줄러 및 브로드캐스트 수신기
        ui/
          components/ # 글래스모피즘 구체 등 공통 UI 컴포넌트
          navigation/ # 컴포즈 네비게이션 루트 및 그래프 구성
          screens/    # 온보딩, 홈, 심호흡 세션, 감정 체크 등 개별 화면
          theme/      # Warm Midnight 디자인 시스템 및 테마
          viewmodel/  # 애플리케이션 상태 관리 ViewModel
      res/            # 다국어 리소스 (values-ko, values-en) 및 벡터 드로어블
  docs/               # 프로젝트 기획 및 그래픽 문서 자산
  store-graphics/     # Google Play 스토어 등록용 자산 (아이콘, 배너, 소개글)
```

---

## 🚀 빠른 시작 (Quick Start)

### 빌드 및 실행 요구조건

- **JDK**: 17 이상
- **Android SDK**: Platform 36
- **Android Build Tools**: 36.0.0

### 빌드 방법

프로젝트 루트 디렉토리에서 아래 명령어로 로컬 빌드 및 테스트를 수행할 수 있습니다.

**Windows PowerShell**:
```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

**Linux / macOS / CI**:
```bash
./gradlew test
./gradlew assembleDebug
```

---

## 🔐 릴리즈 빌드 및 서명 정책

릴리즈 APK 및 AAB는 보안을 위해 로컬 환경 변수 주입을 지원합니다. Play Console 업로드 전에는 로컬 `.keystore/my-upload-key.jks`를 백업해두거나 아래 환경 변수를 명시해 실제 업로드 키로 서명해야 합니다.

- `KEYSTORE_PATH`
- `STORE_PASSWORD`
- `KEY_PASSWORD`
- `KEY_ALIAS` (선택, 기본 `upload`)

```bash
# signed release APK 및 AAB 동시 빌드
./gradlew assembleRelease bundleRelease

# Play Console 제출용 AAB/TXT를 바탕화면으로 내보내기
./gradlew :app:exportReleaseToDesktop
```

### Play 출시 (GitHub Actions)

Play 업로드는 [`.github/workflows/play-release.yml`](.github/workflows/play-release.yml)이 합니다. 올리는 트랙은 지금 쓰는 비공개 테스트(`alpha`) 하나이며, 프로덕션에는 올리지 않습니다.

1. `app/build.gradle.kts`의 `versionCode`(+1)·`versionName`, `CHANGELOG.md`(`## [vX.Y.Z]` 항목), README의 현재 버전, `store-graphics/play-console-current/release-notes.txt`(`<ko-KR>`·`<en-US>` 블록, 각 500자 이하)를 맞춰 main에 머지합니다.
2. `vX.Y.Z` 태그를 push하면 워크플로가 시작되고, GitHub 환경 `play`에서 jeiel85가 승인하면 실행됩니다. 단위 시험 → AAB 빌드 → 업로드 키 SHA-256 고정값 확인 → `alpha` 업로드 → 트랙 상태 출력 순서입니다. AAB와 출시 노트는 Actions 아티팩트로 30일 남습니다.
3. 태그 없이 돌리려면 Actions 화면에서 **Play release → Run workflow**(version, `dry_run`)를 씁니다. `dry_run`은 빌드·서명 확인·트랙 조회까지만 하고 올리지 않습니다.

- 태그 하나에 Play 업로드는 이 워크플로 한 번뿐입니다(이 저장소에 다른 릴리스 워크플로는 없습니다). 태그 push로 올린 버전을 `exportReleaseToDesktop` 산출물로 Play Console에 또 올리지 마세요 — 같은 `versionCode`는 Play가 거부합니다. 바탕화면 내보내기는 로컬 보관·수동 확인용으로만 씁니다.
- 비밀값(업로드 키스토어 base64, 스토어/키 비밀번호, 별칭, Play 서비스 계정 키)은 환경 `play`의 Secrets에만 있습니다. 업로드 키를 바꾸면 Secrets와 워크플로의 `UPLOAD_CERT_SHA256`을 함께 고쳐야 합니다.
- 업로드 스크립트 [`tools/play-publish.ps1`](tools/play-publish.ps1)은 전역 스킬 `play-publish` 스크립트의 복사본입니다.

현재 버전: `v1.0.0`

---

## 📄 라이선스 (License)

본 프로젝트는 **MIT License** 하에 제공됩니다. 상세한 내용은 [LICENSE](LICENSE) 파일을 확인하세요.
