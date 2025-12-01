# BMI 비만도 계산기 앱

Kotlin과 Jetpack Compose를 사용하여 만든 간단한 안드로이드 비만도(BMI) 계산기 앱입니다.

## ✨ 주요 기능

- 키(cm)와 몸무게(kg)를 입력받아 BMI 지수를 계산합니다.
- 계산된 BMI 지수에 따라 **저체중, 정상, 과체중, 비만**으로 결과를 나누어 보여줍니다.
- 결과에 따라 다른 색상과 이미지를 표시하여 시각적인 피드백을 제공합니다.

## 🛠️ 적용된 기술 및 아키텍처

이 프로젝트는 최신 안드로이드 개발 트렌드를 학습하고 적용하는 것을 목표로 합니다.

-   **100% Kotlin + Jetpack Compose**: UI 전체를 Jetpack Compose로 선언형으로 구현했습니다.
-   **ViewModel**: UI 상태와 비즈니스 로직을 `BmiViewModel`로 분리하여 UI 레이어의 복잡성을 줄이고, 화면 회전과 같은 상태 변화에도 데이터를 안전하게 유지합니다.
-   **Navigation-Compose**: 화면 간의 이동을 체계적으로 관리하기 위해 Navigation-Compose 라이브러리를 사용했습니다. `NavHost`와 `NavController`를 통해 화면 흐름을 제어합니다.
-   **단일 액티비티 구조 (Single-Activity Architecture)**: 여러 화면을 Composable 함수로 구현하고, `NavHost`를 통해 전환하여 단일 액티비티 내에서 앱 전체가 동작하도록 설계했습니다.
-   **Material 3**: 최신 Material Design 시스템을 적용하여 UI 컴포넌트를 구성했습니다.

## 📱 스크린샷

| 입력 화면 | 결과 화면 |
| :---: | :---: |
| <img width="352" height="491" alt="image" src="https://github.com/user-attachments/assets/3e9103b8-dfe0-440e-80d5-3016ca43255a" /> | <img width="351" height="555" alt="image" src="https://github.com/user-attachments/assets/afa41c20-4fe3-4509-a8b1-f585880b8353" > |

## 📝 학습 과정

1.  **기본 UI 구성**: Jetpack Compose를 사용하여 기본적인 입력 필드와 버튼을 배치했습니다.
2.  **상태 관리**: `remember`와 `mutableStateOf`를 사용하여 사용자의 입력을 관리하고, 상태 변화에 따라 UI가 업데이트되도록 구현했습니다.
3.  **화면 전환 (상태 기반)**: `if-else` 문을 사용하여 조건에 따라 다른 Composable(화면)을 보여주는 간단한 화면 전환을 구현했습니다.
4.  **화면 전환 (Navigation-Compose)**: 실무에 가까운 구조를 위해 Navigation-Compose를 도입하여, 화면의 경로(route)를 기반으로 하는 체계적인 내비게이션을 구축했습니다.
5.  **ViewModel 도입**: UI 로직과 비즈니스 로직을 분리하기 위해 `ViewModel`을 적용했습니다. 이를 통해 화면 간 데이터를 공유하고, UI의 역할과 책임을 명확히 구분했습니다.

---

*이 README는 AI 어시스턴트와의 대화를 통해 작성되었습니다.*
