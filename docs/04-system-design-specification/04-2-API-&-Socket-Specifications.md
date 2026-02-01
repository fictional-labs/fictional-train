### 1. 통신 원칙 (Communication Principles)

본 프로젝트는 **하이브리드 통신 방식**을 채택한다.

- **HTTP (REST API)**: 정적인 데이터 조회 (초기 그래프 로딩, 전적 조회, 로그인)
- **WebSocket (STOMP)**: 상태를 변화시키는 게임 플레이 (단어 입력, 스트림 탈취, 실시간 알림)

---

### 2. 공통 응답 포맷 (Common Response Format)

모든 HTTP 응답은 아래 JSON 구조를 따른다.

```json
{
  "result": "SUCCESS", // or "ERROR"
  "data": { ... },     // 성공 시 데이터
  "message": null,     // 에러 시 메시지
  "errorCode": null    // 에러 시 코드 (예: E4001)
}
```

---

### 3. REST API 명세 (HTTP)

### 3.1 [GET] 현재 메인스트림 정보 조회

앱 실행 시 가장 먼저 호출하여, 포커싱할 위치를 파악한다.

- **Endpoint**: `GET /api/v1/streams/main`
- **Response**:
    
    ```json
    {
      "streamId": 105,
      "headNodeId": 5023, // 현재 기차의 맨 앞부분 (포커스 위치)
      "founderName": "SeungJun",
      "length": 142
    }
    ```
    

### 3.2 [GET] 노드 자식 조회 (Lazy Loading)

특정 노드의 `+` 버튼을 눌렀을 때, 혹은 초기 진입 시 화면에 보이는 범위의 노드를 가져온다.

- **Endpoint**: `GET /api/v1/nodes/{parentId}/children`
- **Response**:
    
    ```json
    [
      {
        "nodeId": 5024,
        "word": "기차",
        "depth": 15,
        "isDeadEnd": false,
        "authorName": "UserA"
      },
      {
        "nodeId": 5025, // 분기된 다른 형제 노드
        "word": "기린",
        "depth": 15,
        "isDeadEnd": true,
        "authorName": "UserB"
      }
    ]
    ```
    

### 3.3 [GET] 한방 단어 검증 (AI Pre-check)

(옵션) 소켓을 보내기 전, 프론트에서 미리 단어 유효성을 체크할 때 사용.

- **Endpoint**: `GET /api/v1/validation/word?text=이리듐`
- **Response**:
    
    ```json
    {
      "isValid": false,
      "reason": "DEAD_END_WORD", // AI가 판단한 '한방 단어'
      "existsInDictionary": false
    }
    ```
    

---

### 4. WebSocket 프로토콜 명세 (STOMP)

실시간 게임 플레이를 위한 메시지 규격이다.

- **Endpoint**: `ws://domain/ws-stomp`
- **Subscribe Path (구독)**: `/topic/stream/main` (모든 유저가 이곳을 구독)

### 4.1 [SEND] 단어 이어가기 (Play)

유저가 단어를 입력했을 때 서버로 전송.

- **Destination**: `/app/game/submit`
- **Payload**:
    
    ```json
    {
      "parentNodeId": 5023, // 어디 뒤에 잇는가?
      "word": "차표",
      "userId": 10
    }
    ```
    

### 4.2 [RECEIVE] 단어 등록 성공 (Broadcast)

서버가 검증을 마치고 모든 유저에게 "새 노드가 생겼다"고 알림.

- **Destination**: `/topic/stream/main`
- **Event Type**: `NODE_ADDED`
- **Payload**:
    
    ```json
    {
      "type": "NODE_ADDED",
      "node": {
        "id": 5024,
        "word": "차표",
        "parentId": 5023,
        "authorName": "SeungJun"
      }
    }
    ```
    
    - **Action**: 프론트는 이 메시지를 받으면 그래프 끝에 노드를 하나 그림.

### 4.3 [SEND] 스트림 탈취 시도 (Hijack)

유저가 '한방 단어' 티켓을 사용하여 새로운 분기를 만들 때.

- **Destination**: `/app/game/hijack`
- **Payload**:
    
    ```json
    {
      "targetNodeId": 4500, // 과거의 노드 ID (오이)
      "newWord": "이불",
      "userId": 10
    }
    ```
    

### 4.4 [RECEIVE] 메인스트림 변경 알림 (Broadcast)

누군가 탈취에 성공하여 대격변이 일어났을 때.

- **Destination**: `/topic/stream/main`
- **Event Type**: `STREAM_CHANGED`
- **Payload**:
    
    ```json
    {
      "type": "STREAM_CHANGED",
      "newHeadNodeId": 6001, // 새로운 메인스트림의 끝
      "hijackerName": "SeungJun",
      "message": "승준님이 '이리듐'으로 기존 메인 스트림을 탈취하여 새로운 시대를 열었습니다!"
    }
    ```
    
    - **Action**: 프론트는 화면을 흔드는 이펙트를 주고, 카메라를 새로운 Head 위치로 이동시킴. 기존 라인은 회색(Archived) 처리.

---

### 5. 에러 코드 정의 (Error Codes)

API 및 소켓 에러 메시지 표준.

| Code | Message | Description |
| --- | --- | --- |
| **E4001** | `WORD_DUPLICATED` | 이미 해당 노드 뒤에 존재하는 단어임. |
| **E4002** | `INVALID_TURN` | 두음법칙 위반 (기차 -> 나비). |
| **E4003** | `NOT_A_NOUN` | 명사가 아님 (AI 판정). |
| **E4004** | `HIJACK_UNAVAILABLE` | 탈취 권한이 없거나 티켓이 부족함. |
| **E5001** | `AI_SERVER_ERROR` | AI 검증 서버 응답 없음. (일시적 장애) |

---