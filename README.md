## Matching 서비스

### 커뮤니티 기반 플래폼으로 개발자, 디자이너, 기획자 등 실무자들이 소통하고 협업할 수 있는 공간을 제공한다.


1. Q&A 게시판
- 사용자들은 개발, 디자인, 기획 등 다양한 주제로 질문을 게시판에 작성하고, 다른 사용자들이 그에 대한 답변을 제공하는 게시판
- 조회, 삭제, 수정, 스크랩, 댓글, 좋아요 등 기능 제공

2. 모집 게시판
- 사용자들은 사이드 프로젝트나 스터디 등에 참여하거나 참여자를 모집할 수 있는 게시판
- 조회, 삭세, 수정, 스크랩, 채팅 등 기능 구현

3. 검색 기능
- 사용자가 필요한 정보를 제공하기 위해 키워드를 통한 결과 제공
- QueryDSL 활용 각 카테고리에 맞는 결과 제공

4. 실시간 채팅
- 모집 게시판과 연동하여 해당 프로젝트에 참여한 사용자들에게 채팅 기능 제공
- 웹소켓 활용, 실시간으로 사용자 채팅 사용 가능
- RedisDB 활용 성능 개선 및 내용 저장 및 읽기 가능

5. 회원 인증 (JWT)
- 사용자 정보 보안공격에 대비하여 Spring Security 연동 및 설계 단계를 고려하여 개발
- AccessToken은 클라이언트에 제공하고 RefreshToken은 서버 측에서 관리하여 토큰 탈취 및 보안 이슈에 대한 문제점을 줄였고, 만료된 AccessToken 재발급 로직에 대한 구현을 통해 사용자 재로그인과 같은 번거로운 과정을 줄여 편리성제공

6. 이미지 업로드
- 사용자 프로필 및 게시판 업로드 이미지를 AWS 버킷에 업로드하여 관리


### ERD
![matching_erd](https://github.com/0jo-gil/matching_backend/assets/74961404/da48fc99-14ad-4f67-938d-f0ba2bc5943e)

### SKILL
- Java11, SpringBoot, Gradle, MariaDB, Redis, JUnit, Mockito, JPA, JWT, Spring Security, AWS Bucket, WebSocket, Stomp, QueryDSL
