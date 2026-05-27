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
- **로컬 우선 정책 (Offline-First)**: 사용자의 민감한 감정 상태 기록이나 세션 통계 데이터는 외부 서버로 절대 송출되지 않으며, 디바이스의 안전한 격리 샌드박스 내에 암호화 저장됩니다.
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

릴리즈 APK 및 AAB는 보안을 위해 로컬 환경 변수 주입을 지원하며, 기본적으로 프로젝트 루트에 자동 생성된 `my-upload-key.jks`를 통해 자동 Fallback 서명되도록 빌드 파일이 격리 구성되어 있습니다.

```bash
# signed release APK 및 AAB 동시 빌드
./gradlew assembleRelease bundleRelease
```

---

## 📄 라이선스 (License)

본 프로젝트는 **MIT License** 하에 제공됩니다. 상세한 내용은 [LICENSE](LICENSE) 파일을 확인하세요.
