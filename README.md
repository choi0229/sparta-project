# 🎓 스파르타 재직자 부트캠프 MSA 3기 - 프로젝트 저장소

> 매주 진행한 프로젝트의 커밋 내역과 구현 요구사항을 관리하는 저장소입니다.

## 📚 주차별 프로젝트

### [Week 1: E-Commerce API 기본 구현](./week1)
**주제:** 상품/카테고리/주문/환불 관리 시스템
jpa 사용법 익히기!
**구현 기능:**
- ✅ 상품 관리 (Product Management)
  - 상품 등록/조회/수정 API
  
- ✅ 카테고리 관리 (Category Management)
  - 카테고리 등록/조회/수정 API
  
- ✅ 주문 관리 (Order Management)
  - 주문 생성/조회/취소 API
  - 주문 상태 변경 API
  
- ✅ 환불 관리 (Refund Management)
  - 환불 요청/처리/조회 API

**기술 스택:** Spring Boot, JPA, MySQL, docker, Flyway

**상세 요구사항:** [week1/README.md](./week1/README.md)

---


### [Week 2: E-Commerce API With QuertDSL](./week2)
**주제:** 상품/카테고리/주문/환불 관리 시스템
queryDSL 활용
**구현 기능:**
- ✅ 상품 관리 (Product Management)
  - 상품 등록/조회/수정 API
  - QueryDSL 기반 조건부 조회 및 페이징
  - 상품명 unique 제약 및 삭제 검증
  
- ✅ 카테고리 관리 (Category Management)
  - 계층형 카테고리 구조 (Self-Join)
  - 카테고리별 최다 판매 Top 10 조회
  - 하위 카테고리/연관 상품 존재 시 삭제 제한
  
- ✅ 주문 관리 (Order Management)
  - 주문 생성 시 재고 차감 및 트랜잭션 처리
  - 주문 상태별/기간별 조회
  - 주문 취소 시 재고 복원
  
- ✅ 환불 관리 (Refund Management)
  - 환불 요청/처리/조회 API
  - 환불 승인 시 재고 복원 로직

**기술 스택:** Spring Boot, JPA, QueryDSL, MySQL, docker, Flyway

**상세 요구사항:** [week2/README.md](./week2/README.md)

---

### [Week 3: 장바구니 & 멀티 레벨 포인트 시스템](./week3)
**주제:** 장바구니 기반 포인트 적립 및 환불 시스템
인메모리 캐싱 및 단위테스트, mockmvc 테스트 코드작성
**구현 기능:**
- ✅ 사용자 관리
  - 사용자 CRUD API 
  - AOP 기반 감사 로그
  - 단위테스트 및 MockMvc 테스트 진행
  
- ✅ 장바구니 기능
  - 인메모리 기반 장바구 추가/삭제/조회 API
  - 쿠폰/프로모션 배수 적용
  - 단위테스트 및 MockMvc 테스트 진행
  
- ✅ 포인트 시스템
  - PointWallet & PointTransaction 설계
  - Flyway 마이그레이션
  - 포인트 적립/차감/만료 정책
  
- ✅ 환불 시스템
  - 포인트 회수 및 재고 복구
  - 부분 환불 지원
  - 환불 감사 로그
  
- ✅ 리포트
  - QueryDSL 기반 일별 포인트 통계
  - Given-When-Then 테스트

**기술 스택:** Spring Boot, JPA, Flyway, AOP, docker

**상세 요구사항:** [week3/README.md](./week3/README.md)

---

### [Week 4: RAG 시스템 구축](./week4)
**주제:** Open Web UI 연동 및 RAG 기능 실습

**구현 기능:**
- ✅ OpenAI 호환 API 구현
  - `/v1/chat/completions` 엔드포인트
  - 동기/스트리밍 응답 처리
  - SSE (Server-Sent Events) 구현
  
- ✅ Spring AI 연동
  - MessageConverter 서비스
  - ChatClient 통합
  - OpenAI ↔ Spring AI 메시지 변환
  
- ✅ 모델 관리
  - `/v1/models` 목록 제공 API
  - Ollama 어댑터 연동
  - 다중 모델 지원

**기술 스택:** Spring Boot, Spring AI, Ollama, Open Web UI, claude

**상세 요구사항:** [week4/README.md](./week4/README.md)

---

### [Week 5: Docker 환경 & RAG 실습](./week5)
**주제:** Docker Compose 기반 전체 스택 구성

**구현 기능:**
- ✅ Docker 환경 구성
  - PostgreSQL (벡터 저장소)
  - Ollama (LLM 백엔드)
  - Spring API
  - Open Web UI
  
- ✅ RAG 기능 실습
  - 문서 컬렉션 생성 (2개 이상)
  - 다양한 파일 형식 업로드 (10개 이상)
  - 3가지 질문 유형 테스트
  
- ✅ RAG 파라미터 최적화
  - Chunk Size 실험
  - Top K 조정
  - 검색 품질 평가
  
- ✅ 통합 테스트
  - 기본 대화 테스트
  - 스트리밍 동작 확인
  - 모델 전환 테스트

**기술 스택:** Docker, Docker Compose, PostgreSQL, Ollama, Open Web UI

**상세 요구사항:** [week5/README.md](./week5/README.md)

---

### [Week 6: Redis기반 인증 및 장바구니 기능 개발](./week6)
**주제:** 사용자 인증 및 redis기반 인증 및 장바구니

**구현 기능:**
- ✅ 사용자 인증 API 구현
  - 회원 가입/로그인/로그아웃/내 정보 조회 API 기본 구현.
  - 비밀번호 암호화 (예: BCryptPasswordEncoder) 적용.
  
- ✅ 사용자 인증 API Redis 세션 연동
  - Redis 환경 구축 (Docker)
  - Redis 기반 세션으로 동작하도록 검증
  
- ✅ 장바구니 기능 개발
  - 장바구니 등록, 조회, 수정, 삭제 API
  
  
**기술 스택:** Spring Boot, JPA, Transaction Management, redis, docker

**상세 요구사항:** [week6/README.md](./week6/README.md)

---

### [Week 7: 할인 쿠폰 시스템](./week7)
**주제:** 쿠폰 관리 및 오프라인 쿠폰 등록

**구현 기능:**
- ✅ 쿠폰 관리 시스템
  - 쿠폰 CRUD API
  - 할인 유형별 계산 (정률/정액)
  - 최소 주문금액 및 최대 할인금액 적용
  
- ✅ 상품별 최대 할인율 조회
  - 적용 가능한 쿠폰 필터링
  - 최대 할인율 계산 로직
  - 유효 기간 및 사용 한도 검증
  
- ✅ 쿠폰 시스템
  - 쿠폰 코드 대량 발급
  - 중복 방지 (Unique 제약)
  - 쿠폰 등록 및 상태 관리
  
- ✅ 확장 기능 (선택)
  - 사용자별 쿠폰 관리 CRUD
  - 쿠폰 사용 이력 추적

**기술 스택:** Spring Boot, JPA, Transaction Management

**상세 요구사항:** [week7/README.md](./week7/README.md)

---

## 🛠️ 공통 기술 스택

**Backend:**
- Java 17
- Spring Boot 3.2.11, 3.5.7
- Spring Data JPA
- QueryDSL
- Spring Session
- Spring AI

**Database:**
- MySQL 8.0
- PostgreSQL 16
- Redis

**Infra:**
- Docker
- Docker Compose

**Testing:**
- MockMvc
- AssertJ

**Tools:**
- Flyway (Migration)
- Lombok
- Gradle

---

## 📁 프로젝트 구조
```
sparta-msa-bootcamp/
├── week1/                  # E-Commerce 기본 API
├── week2/                  # 장바구니 & 포인트 시스템
├── week3/                  # Redis 세션 & 인증
├── week4/                  # RAG API 구현
├── week5/                  # Docker & RAG 실습
├── week6/                  # 쿠폰 시스템
├── week7/                  # TBD
└── README.md              # 전체 프로젝트 개요 (현재 문서)
```

---

## 🎯 학습 목표

### 핵심 역량
- ✅ RESTful API 설계 및 구현
- ✅ JPA & QueryDSL 활용한 복잡한 쿼리 작성
- ✅ 트랜잭션 관리 및 동시성 제어
- ✅ Redis 기반 세션 및 캐시 관리
- ✅ Docker 기반 개발 환경 구축
- ✅ Spring AI를 활용한 LLM 연동
- ✅ RAG 시스템 구축 및 최적화

### 실무 스킬
- 📊 계층형 데이터 구조 설계
- 🔐 인증/인가 시스템 구현
- 💰 포인트/쿠폰 시스템 설계
- 🤖 AI 기반 챗봇 개발
- 📦 컨테이너 기반 배포
- 🧪 테스트 주도 개발 (TDD)

---

## 📝 각 주차 상세 문서

각 주차별 상세 구현 요구사항, 힌트, 테스트 케이스는 해당 주차 디렉토리의 README를 참고하세요.

- [Week 1 상세 문서](./week1/README.md)
- [Week 2 상세 문서](./week2/README.md)
- [Week 3 상세 문서](./week3/README.md)
- [Week 4 상세 문서](./week4/README.md)
- [Week 5 상세 문서](./week5/README.md)
- [Week 6 상세 문서](./week6/README.md)
- [Week 7 상세 문서](./week7/README.md)

---

## 💬 문의 및 피드백

프로젝트 관련 문의사항이나 개선 제안은 Issues를 통해 남겨주세요.

---

**Last Updated:** 2025-12-19
