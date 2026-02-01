### [Git Flow 전략]

- `main`: 배포 가능한 안정 버전 (Production)
- `develop`: 개발 중인 버전 (Staging)
- `feature/{기능명}`: 단위 기능 개발 브랜치
    - 예: `feature/login`, `feature/game-logic`

### [Commit Message 규칙]

- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `docs`: 문서 수정
- `refactor`: 코드 리팩토링 (기능 변경 없음)
- `test`: 테스트 코드 추가
    - 예: `feat: 메인스트림 탈취 로직 구현 (#12)`

### [Response Wrapper (공통 응답 객체)]

```java
public record ApiResponse<T>(
    ResultType result, // SUCCESS, ERROR
    T data,
    String message
) {}
```

---