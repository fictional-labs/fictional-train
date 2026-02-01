---

<div align="center">

<img src="frontend/public/favicon.ico" width="80" alt="project-logo">

# Fictional-Train

실시간 **스트림 탈취형 끝말잇기** 플랫폼

</div>

---

<br>

## 문서 목차

### 1. 프로젝트 기획서
- [프로젝트 개요](docs/01-project-proposal/01-1-Project-Overview.md)
- [기획 의도](docs/01-project-proposal/01-2-Planning-Intent.md)
- [기대 효과](docs/01-project-proposal/01-3-Expected-Effects.md)

### 2. 프로젝트 계획 및 컨벤션
- [기술 스택 선정 이유](docs/02-project-plan-and-convention/02-1-Tech-Stack.md)
- [코드 컨벤션](docs/02-project-plan-and-convention/02-2-Code-Convention.md)

### 3. 시스템 요구사항 명세서
- [서비스 정책](docs/03-system-requirement-specification/03-1-Service-Policy.md)
- [기능 요구사항](docs/03-system-requirement-specification/03-2-Functional-Requirements.md)
- [비기능 요구사항](docs/03-system-requirement-specification/03-3-Non-Functional-Requirements.md)

### 4. 시스템 설계 명세서
- [데이터베이스 스키마 설계](docs/04-system-design-specification/04-1-Database-Schema-Design.md)
- [API 및 소켓 명세](docs/04-system-design-specification/04-2-API-&-Socket-Specifications.md)
- [핵심 로직 시퀀스 다이어그램](docs/04-system-design-specification/04-3-Core-Logic-Sequence-Diagram.md)
- [시스템 아키텍처](docs/04-system-design-specification/04-4-System-Architecture.md)

### 5. 프로젝트 최종 보고서
- [구현 및 시연](docs/05-project-final-report/05-1-Implementation-and-Demonstration.md)
- [트러블 슈팅 (문제 해결)](docs/05-project-final-report/05-2-Troubleshooting-and-Solutions.md)
- [성능 및 최적화](docs/05-project-final-report/05-3-Performance-and-Optimization.md)

<br>

## 기술 스택

<table>
  <thead>
    <tr>
      <th align="center">영역</th>
      <th align="center">분류</th>
      <th align="center">사용 기술</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center" rowspan="3"><b>백엔드</b></td>
      <td>언어 및 프레임워크</td>
      <td>
        <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white">
        <img src="https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=flat-square&logo=springboot&logoColor=white">
      </td>
    </tr>
    <tr>
      <td>AI 및 빌드 도구</td>
      <td>
        <img src="https://img.shields.io/badge/Spring%20AI-0.8-6DB33F?style=flat-square&logo=spring&logoColor=white">
        <img src="https://img.shields.io/badge/Gradle-8.5-02303A?style=flat-square&logo=gradle&logoColor=white">
      </td>
    </tr>
    <tr>
      <td>데이터 및 저장소</td>
      <td>
        <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white">
        <img src="https://img.shields.io/badge/Redis-7.0-DC382D?style=flat-square&logo=redis&logoColor=white">
      </td>
    </tr>
    <tr>
      <td align="center" rowspan="3"><b>프론트엔드</b></td>
      <td>UI 프레임워크</td>
      <td>
        <img src="https://img.shields.io/badge/Vue.js-3.0-4FC08D?style=flat-square&logo=vue.js&logoColor=white">
      </td>
    </tr>
    <tr>
      <td>상태 관리 및 빌드</td>
      <td>
        <img src="https://img.shields.io/badge/Pinia-State-yellow?style=flat-square&logo=pinia&logoColor=black">
        <img src="https://img.shields.io/badge/Vite-Build-646CFF?style=flat-square&logo=vite&logoColor=white">
      </td>
    </tr>
    <tr>
      <td>시각화</td>
      <td>
        <img src="https://img.shields.io/badge/Vue%20Flow-Graph-4FC08D?style=flat-square">
      </td>
    </tr>
  </tbody>
</table>

<br>

## 실행 방법

### 1. 인프라 실행
DB(MySQL)와 캐시(Redis)가 먼저 실행되어야 합니다.
```bash
cd backend
docker-compose up -d
```

### 2. 백엔드 실행
```bash
# OpenAI API Key 설정 필요 (application.yml)
./gradlew bootRun
```

### 3. 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```
