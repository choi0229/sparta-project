# Week 1: E-Commerce API 기본 구현

> **학습 목표:** JPA 사용법 익히기 및 기본 CRUD API 구현

## 📋 목차

- [프로젝트 개요](#-프로젝트-개요)
- [기술 스택](#-기술-스택)
- [ERD 설계](#-erd-설계)
- [구현 요구사항](#-구현-요구사항)
- [프로젝트 구조](#-프로젝트-구조)
- [실행 방법](#-실행-방법)
- [학습 내용](#-학습-내용)

---

## 🎯 프로젝트 개요

E-Commerce 플랫폼의 핵심 기능인 **상품, 카테고리, 주문, 환불** 관리 시스템을 구현합니다.

JPA를 활용한 기본적인 CRUD 작업과 엔티티 간의 연관관계 매핑을 학습합니다.

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.5.7 |
| **ORM** | Spring Data JPA |
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
│    Category      │       │     Product      │       │      Orders      │
├──────────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (PK)          │       │ id (PK)          │       │ id (PK)          │
│ name             │◄──────│ category_id (FK) │       │ user_id (FK)     │
│ description      │   1:N │ name             │       │ total_price      │
│ parent_id (FK)   │       │ description      │       │ status           │
│ created_at       │       │ price            │       │ shipping_address │
│ updated_at       │       │ stock            │       │ created_at       │
└──────────────────┘       │ created_at       │       │ updated_at       │
         ↕                 │ updated_at       │       └──────────────────┘
    Self-Join              └──────────────────┘              │
   (계층 구조)                      │                        │ 1
                                   │                        │
                                   │ N                      │ N
                                   │                        │
                                   └────────┬───────────────┘
                                            │
                                            ▼
                                   ┌──────────────────┐
                                   │  Order_Product   │
                                   ├──────────────────┤
                                   │ id (PK)          │
                                   │ order_id (FK)    │
                                   │ product_id (FK)  │
                                   │ quantity         │
                                   │ price            │
                                   │ created_at       │
                                   │ updated_at       │
                                   └──────────────────┘
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

**1. User ↔ Orders (1:N)**
- 한 사용자는 여러 주문을 할 수 있음
- `orders.user_id` → `user.id`

**2. Category ↔ Category (Self-Join)**
- 계층형 카테고리 구조
- `category.parent_id` → `category.id`
- 최상위 카테고리는 `parent_id`가 NULL

**3. Category ↔ Product (1:N)**
- 한 카테고리에 여러 상품이 속함
- `product.category_id` → `category.id`

**4. Orders ↔ Order_Product (1:N)**
- 한 주문에 여러 상품이 포함될 수 있음
- `order_product.order_id` → `orders.id`
- **중간 테이블 역할:** 주문 시점의 가격, 수량 저장

**5. Product ↔ Order_Product (1:N)**
- 한 상품은 여러 주문에 포함될 수 있음
- `order_product.product_id` → `product.id`

**6. Orders ↔ Refund (1:1 or 1:N)**
- 한 주문에 대해 환불 요청 가능
- `refund.order_id` → `orders.id`
- `refund.user_id` → `user.id`

---

## 📝 구현 요구사항

### 1. 상품 관리 (Product Management)

#### ✅ 필수 구현

**상품 등록 API**
- 필수 입력 필드: `name`, `price`, `stock`, `category_id`
- 상품 등록 시 카테고리 존재 여부 검증
- 재고는 0 이상이어야 함

**상품 조회 API**
- 전체 상품 목록 조회 (페이징 지원)
- 단일 상품 상세 조회
- 검색 조건:
  - 카테고리 ID
  - 가격 범위 (minPrice, maxPrice)
  - 상품명 키워드 (LIKE 검색)

**상품 수정 API**
- 변경 가능 필드: `name`, `description`, `price`, `stock`, `category_id`
- 존재하지 않는 상품 수정 시 404 반환


---

### 2. 카테고리 관리 (Category Management)

#### ✅ 필수 구현

**카테고리 등록 API**
- 필수 입력 필드: `name`, `description`
- 카테고리명 중복 검증 (선택)

**카테고리 조회 API**
- 전체 카테고리 목록 조회
- 카테고리별 상품 개수 포함

**카테고리 수정 API**
- 변경 가능 필드: `name`, `description`

---

### 3. 주문 관리 (Order Management)

#### ✅ 필수 구현

**주문 생성 API**
- 필수 입력 필드: `user_id`, `product_id`, `quantity`, `shipping_address`
- 주문 생성 프로세스:
  1. 상품 존재 여부 확인
  2. 재고 충분한지 검증
  3. 재고 감소 처리
  4. 총 금액 계산 (`price * quantity`)
  5. 주문 상태: `PENDING`으로 초기화

**주문 조회 API**
- 사용자별 주문 목록 조회
- 필터링: 주문 상태별

**주문 상태 변경 API**
- 상태 전환 규칙:
  - `PENDING` → `COMPLETED`
  - `PENDING` → `CANCELED`
- 유효하지 않은 상태 전환 시 400 반환

**주문 취소 API**
- `PENDING` 상태만 취소 가능
- 취소 시:
  1. 상태를 `CANCELED`로 변경
  2. 상품 재고 복원


---

### 4. 환불 관리 (Refund Management)

#### ✅ 필수 구현

**환불 요청 API**
- 필수 입력 필드: `user_id`, `order_id`, `reason`
- 환불 가능 조건 검증:
  - 주문이 `COMPLETED` 상태
  - 이미 환불 요청된 주문이 아닐 것

**환불 처리 API (관리자)**
- 환불 승인 시:
  1. 환불 상태를 `APPROVED`로 변경
  2. 상품 재고 복원
  3. 주문 상태를 `CANCELED`로 변경
- 환불 거절 시:
  - 환불 상태를 `REJECTED`로 변경

**환불 조회 API**
- 사용자별 환불 요청 목록 조회
- 필터링: 환불 상태별


## 📚 학습 내용

### 1. JPA 기초

**학습 포인트:**
- ✅ 엔티티 설계 및 어노테이션 (`@Entity`, `@Table`, `@Column`)
- ✅ 기본 키 생성 전략 (`@GeneratedValue`)
- ✅ 연관관계 매핑 (`@ManyToOne`, `@OneToMany`)
- ✅ 지연 로딩 vs 즉시 로딩 (`FetchType.LAZY`, `FetchType.EAGER`)


### 2. Spring Data JPA

**학습 포인트:**
- ✅ JpaRepository 인터페이스
- ✅ 쿼리 메서드 작성 규칙
- ✅ @Query 어노테이션 활용
- ✅ 페이징 처리 (Pageable, Page)

### 3. 트랜잭션 관리

**학습 포인트:**
- ✅ @Transactional 어노테이션
- ✅ 트랜잭션 범위 및 전파 속성
- ✅ 롤백 조건 설정


### 4. Flyway 마이그레이션

**학습 포인트:**
- ✅ 데이터베이스 버전 관리
- ✅ SQL 마이그레이션 파일 작성
- ✅ 마이그레이션 전략

---

**학습:**
- 트랜잭션 내에서 조회한 엔티티는 영속 상태
- 엔티티 변경 시 자동으로 DB에 반영 (변경 감지)


---

## 💬 회고
- JPA를 처음 사용하다보니 적응하느라 개발 시간이 조금 오래 걸렸다. 하지만 프로젝트를 진행하면서 어느정도 적응이 되었고, 더티체킹, N+1 등을 이해하면서 공부가 됐던 것 같다.

### 😊 잘한 점
- JPA 기본 개념을 실습으로 체득
- 트랜잭션 관리의 중요성 이해
- Docker로 개발 환경 통일

### 🤔 아쉬운 점
- 복잡한 조회 조건 처리 어려움
- 테스트 코드 부족
- 예외 처리 일관성 부족


---

**Last Updated:** 2025-01-XX
