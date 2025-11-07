# 🧩 MSA 3기 프로젝트 레포지토리

## 📘 제출 방법

### 1️⃣ 브랜치 생성 및 작업

처음에는 `main` 브랜치만 존재합니다. 아래 순서에 따라 **작업용 브랜치(work)** 와 **제출용 브랜치(project)** 를 생성합니다.

#### 브랜치 이름 형식
- 작업용 브랜치: `work/{휴대전화번호}-{영문 이름}`
- 제출용 브랜치: `project/{휴대전화번호}-{영문 이름}`

예시:  
`work/010-1234-5678-paul`  
`project/010-1234-5678-paul`

---

## 🚀 단계별 가이드

## 주의
**절대 main 브랜치에 머지 / 푸시 하지 않습니다.**

### 1. 레포지토리 복제
```bash
git clone https://github.com/msa-3-project-repository.git
cd msa-3-project-repository
git fetch origin
git checkout -b work/010-1234-5678-paul
git status
git add .
git commit -m "feat: 첫 번째 과제 구현"
git push origin work/010-1234-5678-paul

### 2. PR(Pull Request) 생성
1.	GitHub 저장소 페이지로 이동
2.	Compare & Pull Request 클릭
3.	base 브랜치 → project/010-1234-5678-paul
4.	compare 브랜치 → work/010-1234-5678-paul
5.	제목과 내용을 작성 후 Create Pull Request 클릭

💡 project/... 브랜치는 존재하지 않아도 PR 생성 시 자동으로 만들어집니다.

### 3. 병합(선택사항)
```sh
git checkout -b project/010-1234-5678-paul
git merge work/010-1234-5678-paul
git push origin project/010-1234-5678-paul
```

