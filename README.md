# Week 2: E-Commerce API With QueryDSL

> **학습 목표:** QueryDSL 활용 및 복잡한 쿼리 작성

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [ERD 설계](#-erd-설계)
- [구현 요구사항](#-구현-요구사항)
- [프로젝트 구조](#-프로젝트-구조)
- [학습 내용](#-학습-내용)

---

## 🎯 프로젝트 개요

Week 1에서 구현한 E-Commerce API를 **QueryDSL**로 고도화합니다.

복잡한 동적 쿼리, 조건부 조회, 그리고 계층형 데이터 구조를 QueryDSL로 효율적으로 처리하는 방법을 학습합니다.

**주요 개선 사항:**
- QueryDSL을 활용한 동적 쿼리 작성
- 상품명 unique 제약 및 삭제 검증
- 계층형 카테고리 구조 (Self-Join)
- 카테고리별 판매 통계 Top 10
- 다중 정렬 및 복합 조건 검색

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.3.5 |
| **ORM** | Spring Data JPA |
| **Query DSL** | QueryDSL 5.0.0 |
| **Database** | MySQL 8.0 |
| **Migration** | Flyway |
| **Build Tool** | Gradle |
| **Containerization** | Docker, Docker Compose |

---

## 📊 ERD 설계

### 주요 엔티티
```
┌──────────────────┐
│      User        │
├──────────────────┤
│ id (PK)          │
│ name             │
│ email (UK)       │
│ password_hash    │
│ created_at       │
│ updated_at       │
└──────────────────┘
         │
         │ 1
         │
         │ N
         ▼
┌──────────────────┐       ┌──────────────────┐       ┌──────────────────┐
│    Category      │       │     Product      │       │    Purchase      │
├──────────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (PK)          │       │ id (PK)          │       │ id (PK)          │
│ name             │◄──────│ category_id (FK) │       │ user_id (FK)     │
│ description      │   1:N │ name             │       │ total_price      │
│ parent_id (FK)   │       │ description      │       │ status           │
│ created_at       │       │ price            │       │ shipping_address │
│ updated_at       │       │ stock            │       │ created_at       │
└──────────────────┘       │ created_at       │       │ updated_at       │
         ↕                 │ updated_at       │       └──────────────────┘
    Self-Join              └──────────────────┘             │
   (계층 구조)                      │                        │ 1
                                   │                        │
                                   │ N                      │ N
                                   │                        │
                                   └────────┬───────────────┘
                                            │
                                            ▼
                                   ┌──────────────────────┐
                                   │ Purchase_Product     │
                                   ├──────────────────────┤
                                   │ id (PK)              │
                                   │ order_id (FK)        │
                                   │ product_id (FK)      │
                                   │ quantity             │
                                   │ price (주문시점)      │
                                   │ created_at           │
                                   │ updated_at           │
                                   └──────────────────────┘
                                            ▲
                                            │ N
                                            │
                                            │ 1
                                   ┌──────────────────┐
                                   │     Refund       │
                                   ├──────────────────┤
                                   │ id (PK)          │
                                   │ user_id (FK)     │
                                   │ order_id (FK)    │
                                   │ reason           │
                                   │ status           │
                                   │ created_at       │
                                   │ updated_at       │
                                   └──────────────────┘
```

### 연관관계 상세

**1. User ↔ Purchase (1:N)**
```
User 한 명이 여러 주문(Purchase)을 생성
- purchase.user_id → user.id (FK)
```

**2. Category ↔ Category (Self-Join)**
```
계층형 카테고리 구조 (부모-자식 관계)
- category.parent_id → category.id (FK)
- parent_id가 NULL이면 최상위 카테고리
- 예시: 전자기기(부모) → 노트북(자식) → 게이밍 노트북(손자)
```

**3. Category ↔ Product (1:N)**
```
한 카테고리에 여러 상품 포함
- product.category_id → category.id (FK)
```

**4. Purchase ↔ Purchase_Product (1:N)**
```
한 주문에 여러 상품 포함 가능
- purchase_product.order_id → purchase.id (FK)
- 주문 시점의 가격(price)과 수량(quantity) 저장
```

**5. Product ↔ Purchase_Product (1:N)**
```
한 상품이 여러 주문에 포함될 수 있음
- purchase_product.product_id → product.id (FK)
```

**6. Purchase ↔ Refund (1:N)**
```
한 주문에 대해 환불 요청 가능
- refund.order_id → purchase.id (FK)
- refund.user_id → user.id (FK)
```

---

## 📝 구현 요구사항

### 1. 상품 관리 API (Product Management)

#### ✅ 필수 구현

**상품 등록 API**
- 필수 입력 필드: `name`, `description`, `price`, `stock`, `category_id`
- **상품 이름 unique 제약 조건** 적용
- **QueryDSL을 활용한 등록 후 ID 반환**

**상품 수정 API**
- 변경 가능 필드: `name`, `description`, `price`, `stock`, `category_id`
- 상품 ID 기반 수정

**상품 삭제 API**
- **상품 삭제 시 주문 상태 검증**
  - `COMPLETED` 상태의 주문이 있으면 삭제 불가
  - 검증 후 삭제 처리

**상품 조회 API**
- **QueryDSL 조건부 조회:**
  - 카테고리 ID
  - 가격 범위 (minPrice, maxPrice)
  - 재고 상태 (재고 없음 포함/제외)
  - 상품명 키워드
- **페이징 처리 및 정렬 옵션:**
  - 가격순 (오름차순/내림차순)
  - 최신순
  - 다중 정렬 지원

#### 🚀 도전 과제

- **다중 정렬 조건** 지원 (가격 + 등록일 조합)
- **재고 부족 상품 필터링** ("재고 부족" 표시)
- **QueryDSL BooleanBuilder** 복합 조건 검색

---

### 2. 카테고리 관리 API (Category Management)

#### ✅ 필수 구현

**카테고리 등록 API**
- 필수 입력 필드: `name`, `description`, `parent_id`
- **카테고리 이름 중복 방지**
- **계층형 구조 지원** (트리 구조)

**카테고리 수정 API**
- 변경 가능 필드: `name`, `description`, `parent_id`
- **부모 카테고리 변경 가능**

**카테고리 삭제 API**
- **삭제 제한 조건:**
  - 하위 카테고리가 없어야 함
  - 연관된 상품이 없어야 함

**카테고리 조회 API**
- 전체 카테고리 목록 조회
- **카테고리별 최다 판매 Top 10**

#### 🚀 도전 과제

- **카테고리별 판매량 집계 Top 10**
  - LEFT JOIN + GROUP BY 활용
- **카테고리 전체 트리 구조 재귀 조회**
- **계층 레벨별 정렬**

---

### 3. 주문 관리 API (Order Management)

#### ✅ 필수 구현

**주문 생성 API**
- 필수 입력 필드: `user_id`, `product_id`, `quantity`, `shipping_address`
- **재고 차감 및 트랜잭션 처리**
- 주문 상태: `PENDING`

**주문 조회 API**
- **QueryDSL 동적 필터링:**
  - 사용자 ID
  - 주문 상태 (PENDING, COMPLETED, CANCELED)
  - 날짜 범위
- **페이징 및 정렬 지원**

**주문 취소 API**
- `PENDING` 상태만 취소 가능
- **재고 복원 처리**

#### 🚀 도전 과제

- **주문 상태 변경 이력 관리** (order_history 테이블)
- **동시성 문제 해결** (@Version, 비관적 락)
- **QueryDSL 복합 필터링**

---

### 4. 환불 관리 API (Refund Management)

#### ✅ 필수 구현

**환불 요청 API**
- 필수 입력 필드: `user_id`, `order_id`, `reason`
- **주문 상태 검증** (COMPLETED만 환불 가능)
- 환불 상태: `PENDING`

**환불 처리 API**
- **환불 승인:** 재고 복원 + 주문 상태 변경
- **환불 거절:** 상태만 변경

**환불 조회 API**
- QueryDSL 상태별 필터링
- 사용자별 / 관리자 전체 조회

#### 🚀 도전 과제

- **환불 처리 시 트랜잭션 병합**
- **관리자 통계 API** (상태별 환불 건수)
- **결제 취소 Mock API**

---


## 📚 학습 내용

### 1. QueryDSL 기초

**학습 포인트:**
- ✅ QueryDSL 설정 및 의존성 추가
- ✅ Q-Type 클래스 생성 및 활용
- ✅ JPAQueryFactory 사용법
- ✅ BooleanBuilder를 통한 동적 쿼리


### 2. 동적 쿼리 작성

**학습 포인트:**
- ✅ BooleanBuilder 활용
- ✅ 조건부 WHERE 절 구성
- ✅ 동적 정렬 (OrderSpecifier)
- ✅ Projection을 통한 DTO 변환


### 3. 계층형 데이터 구조

**학습 포인트:**
- ✅ Self-Join을 통한 계층 구조
- ✅ 재귀 쿼리 구현
- ✅ 트리 구조 탐색


### 4. 집계 쿼리

**학습 포인트:**
- ✅ GROUP BY 활용
- ✅ 집계 함수 (SUM, COUNT, AVG)
- ✅ HAVING 조건
- ✅ JOIN + GROUP BY 조합


---

## 💬 회고
- queryDSL을 도입함으로써 복잡함 쿼리도 쉽게 적용이 가능하다는 편리함을 느꼈다. 코드를 짤 때 멘토님이 강조하신 유지보수가 용이한 코드를 짜기 위해 노력해서 booleanexpression을 이용하여 재사용성을 확장 시키는 것도 좋은 경험이었다.

### 😊 잘한 점
- QueryDSL을 활용한 복잡한 동적 쿼리 작성 능력 향상
- 계층형 데이터 구조 설계 및 구현 경험

### 🤔 아쉬운 점
- QueryDSL 성능 최적화 부족
- 복잡한 집계 쿼리 작성 시행착오


---

**Last Updated:** 2025-12-19
