
# 🍇[포도알] 극장 좌석 리뷰사이트🍇


## 🎉 포도알 소개 | About Us

### 극장 좌석?! 좋은 자리는 없는 상태에 최선의 선택은?
![KakaoTalk_20221003_034649228](https://user-images.githubusercontent.com/109055420/193470896-90e12165-8d97-4437-becd-624c4b39415a.png)

👉 나만 알고 있던 좋은 좌석이나 꿀 같은 좌석을 공유하고 싶으신 분! </br>
👉 키 큰 사람도 키 작은 사람도 공연이 다 보이는 자리를 공유하고 싶으신 분 !</br>
👉 극장 좌석이 어떻게 보일지 모르겠다면 포도알에서 확인하세요! </br>

- 포도알은 숨겨진 꿀 자리나 최선의 좌석 선택을 도울 수 있는 웹 사이트 입니다.

### 🌎 웹사이트 | Website  [포도알 바로가기](https://podoal.net)


<br>

## 🔭목차 | Contents
1. [개발기간 | Project Period](#-개발기간--project-period)
2. [아키텍쳐 | Architecture](#-아키텍쳐--architecture)
3. [주요 기능 | Main Function](#-주요-기능--Main-Function)
4. [개발환경 | Development Enviornment](#-개발환경--development-environment)
5. [라이브러리 | Library](#-라이브러리--library)
6. [ERD](#-erd)
7. [트러블 슈팅| Trouble shooting](#-트러블-슈팅--trouble-shooting)
8. [ 팀원 | TEAM](#-팀원--team)

<br>



## ⌚ 개발기간 | Project Period
2022.08.26 ~ 2022.10.03 (6주간)

<br>

## 🛠 아키텍쳐 | Architecture
![KakaoTalk_20221003_032628588](https://cdn.discordapp.com/attachments/457223932244656128/1026435122318290994/Architecture.png)


## ⚔ 주요 기능 | Main Function
### FE
- 소셜 로그인 (트위터 / 카카오)
- 실시간 리뷰 확인 (메인 페이지 실시간 리뷰 SECTION)
- 리뷰 후기 (CRUD) / 리뷰 댓글 / 좋아요 기능 / 리뷰 페이지 무한스크롤
- 태그, 필터, 검색어를 통한 검색 기능
- 프로필 수정
- 프로필 내에서 내가 쓴 리뷰 확인
- 극장 정보 인포 MODAL (KAKAO MAP)
- 반응형

### BE
- 간편한 소셜 로그인 트위터, 카카오톡 지원
- 다양한 경우의 검색 요청 일괄 처리 (QueryDSL)
- 자주 조회하는 데이터 캐싱 처리 (Redis, Spring Cache)
- 배포된 서버의 에러 로그를 쉽게 확인 가능 (Slack Webhook, Logback)
- RefreshToken 토큰을 사용하여 로그인 정보 자동 갱신 (JWT)
- S3 스토리지에 저장되는 이미지 리사이징 처리 (Imgscalr)
- CI/CD 자동 배포 (Githib Action, S3, CodeDeploy)
- 무중단 배포 (NGINX)


## ⛏ 개발환경 | Development Environment

<img  src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img  src="https://img.shields.io/badge/react query-FF4154?style=for-the-badge&logo=reactquery&logoColor=black">
<img  src="https://img.shields.io/badge/Recoil-0088CC?style=for-the-badge&logo=recoil&logoColor=white">
<img  src="https://img.shields.io/badge/React Hook Form-EC5990?style=for-the-badge&logo=react Hook Form&logoColor=white">
<img  src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white">
<img  src="https://img.shields.io/badge/styled-components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white">
<img  src="https://img.shields.io/badge/React Router-CA4245?style=for-the-badge&logo=React Router&logoColor=white">
<img  src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black">
<img  src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img  src="https://img.shields.io/badge/aws Cloundfront-EF2D5E?style=for-the-badge&logo=&logoColor=white">
<img  src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img  src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
<img  src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
<img  src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">
<img  src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=S&logoColor=white">
<img  src="https://img.shields.io/badge/Query DSL-4695EB?style=for-the-badge&logo=&logoColor=white">
<img  src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
<img  src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white">
<img  src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img  src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<img  src="https://img.shields.io/badge/Slack Webhook-4A154B?style=for-the-badge&logo=&logoColor=white">


## 🎨 라이브러리 | Library

     "@fortawesome/react-fontawesome": "^0.2.0",
     "axios": "^0.27.2",
     "file-loader": "^6.2.0",
     "json-server": "^0.17.0",
     "moment": "^2.29.4",
     "react": "^18.2.0",
     "react-dom": "^18.2.0",
     "react-hook-form": "^7.34.2",
     "react-intersection-observer": "^9.4.0",
     "react-query": "^3.39.2",
     "react-router-dom": "^6.3.0",
     "react-scripts": "5.0.1",
     "react-select": "^5.4.0",
     "react-toastify": "^9.0.8",
     "recoil": "^0.7.5",
     "styled-components": "^5.3.5",
     "sweetalert2": "^11.4.33",
     "sweetalert2-react-content": "^5.0.3",
     "swiper": "^8.3.2",
     

    

<br>

## 🔑 ERD 

![erd](https://cdn.discordapp.com/attachments/457223932244656128/1026447296243695697/unknown.png)


<br>


## 🛠 트러블 슈팅 | Trouble shooting



<details>
<summary>(FE) 웹 페이지 성능 최적화 하기</summary>
<div markdown="1">
<span style="color:Red"> 트러블 이슈</span>

![Untitled](https://user-images.githubusercontent.com/108280991/193574430-9f179b4f-1fe9-48d2-9459-6609e7669820.png)

lighthouse를 이용한 성능 측정 결과 성능 및 접근성, 권장사항의 점수가 만족스럽게 나오지 않아 해당 사항들을 정리하고, 개선하기로 결정.

<span style="color:Red"> 트러블 슈팅</span>

이미지 렌더링 시간을 줄이기 위해  

- 차세대 형식을 사용해 이미지를 제공 jpg, png > webp로 변경
- 동적 이미지 캐싱처리 진행. s3, cloudfront 에서 캐싱 정책 변경

<br>

메인페이지 렌더링을 차단시키고 리소스를 먼저 렌더링 하려는걸 방지.  
(리소스 링크에 async를 넣어 같이 로딩 시키도록함.)

<br>

웹폰트가 로딩되기 전까지 텍스트가 표시되지 않도록 최적화를 진행함.  
(font-display : swap;)

<br>

`lighthouse 결과`  

![Untitled (1)](https://user-images.githubusercontent.com/108280991/193575940-42df99e7-2714-4249-90dd-3595916b5a01.png)


</div>
</details>

<details>
<summary>(FE) 이미지 렌더링 속도 개선</summary>
<div markdown="2">
<span style="color:Red">트러블 이슈</span> 
<br> 
사용자가 뮤지컬 좌석 리뷰를 확인하기 위해 여러 서로다른 여러가지 뮤지컬을 조회하고 다시 같은 뮤지컬을 조회할 때마다 렌더링이 계속 일어난다면 사용자 피로도가 올라가 이탈률이 높아질 것으로 예상 

![Untitled (2)](https://user-images.githubusercontent.com/108280991/193578901-b6078872-a15c-4586-9fbd-b66cc1833b1c.png)


<span style="color:Red">트러블 슈팅</span>  
리액트 쿼리의 캐싱 기능을 사용해 바로 사용자에게 렌더링 된 화면을 출력

![Untitled (3)](https://user-images.githubusercontent.com/108280991/193579112-108b09f8-cc5c-4315-a885-5c299b70ba85.png)


</div>
</details>


<details>
<summary>(BE) 극장별 좌석 조회시 데이터 처리 성능 개선</summary>
<div markdown="3">
<span style="color:Red">문제상황</span>  

- 극장별 좌석 정보를 다음과 같은 형태로 테이블에 저장하였습니다.

| 좌석아이디 | 층 | 구역 | 열 | 좌석번호 | 극장아이디 |
|--|--|--|--|--|--|
| 1 | FIRST | A | 1 | 8 | 1 |
| 2 | FIRST |A| 1 | 9 | 1 |

- 극장의 좌석 정보 데이터를 json 형태로 가공하는 과정에서 여러번의 쿼리 호출이 필요했고 서버 응답에 많은 시간이 소요되었습니다.

<span style="color:Red">의견조율</span> 

1. mongoDB 같은 NoSQL 데이터베이스를 사용하여 좌석 정보를 json 형식으로 저장하기
2. 캐시를 적용하여 데이터베이스 호출 횟수를 줄이기

- 극장별 좌석 정보는 변경이 극도로 적고 조회가 많은 데이터 특성을 가지고 있음
- 캐시를 적용하기로 결정

<span style="color:Red">결과</span>  
- JMeter를 활용한 테스트 결과 약 90%의 응답시간 감소를 확인할 수 있었습니다.  

| 라벨 | 표본수 | 평균(ms) | 최소값 | 최대값 | 표준편차 | 오류 |
|--|--|--|--|--|--|--|
| cache | 5000 | 341 | 5 | 725 | 156.5247 | 0 |
| noCache | 5000 | 3284 | 43 | 12071 | 1014.649 | 0 |

<br> 
</div>
</details>



<details>
<summary>(BE) 극장별 좌석 조회시 데이터 처리 성능 개선</summary>
<div markdown="4">
<span style="color:Red">문제상황</span>  

- JWT 토큰을 포함하는 모든 요청은 필터에서 해당 토큰을 검증하는 과정을 거칩니다.
- MemberRepository를 통해 Member 객체를 찾아와서 UserDetails를 구성하였는데

```Java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Member member;
		...
}

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(subject))
                .orElseThrow(() -> new UsernameNotFoundException("등록되지않은 사용자입니다."));
        return new UserDetailsImpl(member);
    }
}
```

- 사용자 권한이 필요 없는 요청에도 토큰이 포함되어있다면 UserDetails를 생성하는 과정에서 데이터베이스를 조회하여 응답시간이 길어지는 것을 확인하였습니다.

<span style="color:Red">수정방향</span>

1. JWT Token은 서버에서 SecretKey를 사용하여 인증한 것
2. Token에 담긴 아이디는 토큰이 유효하다면 정확한 것

- UserDetaileService를 삭제하고, UserDetailsImpl에 Member 객체 대신 Id를 추가하고

- JwtTokenProvider에서 인증정보를 조회할 때, JWT 토큰의 Sub 필드에 저장해둔 id값으로 UserDetails를 생성하였습니다.


```Java
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long memberId;
		...
}

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
		...
		public Authentication getAuthentication(String jwtToken) {
			  Claims claims = getClaims(jwtToken);
			  UserDetailsImpl userDetails = new UserDetailsImpl(Long.parseLong(laims.getSubject()));
			  return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
		}
		...
}
```

- 각각의 엔티티들과 Member 객체가 맺은 연관관계는 제거한 후에 Auditing 속성에 @CreatedBy @LastModifiedBy 어노테이션을 추가하였습니다.

```Java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

		...

    @CreatedBy // 생성자
    private Long createdBy;

    @LastModifiedBy //수정자
    private Long modifiedBy;
}

@RequiredArgsConstructor
@Component
public class LoginUserAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Optional.ofNullable(userDetails.getMemberId());
    }
}
```

<span style="color:Red">결과</span>

- JMeter를 활용한 테스트 결과 약 80%의 응답시간 감소를 확인할 수 있었습니다.


| 라벨 | 표본수 | 평균(ms) | 최소값 | 최대값 | 표준편차 | 오류 |
|--|--|--|--|--|--|--|
| 멤버 객체 사용 | 5000 | 939 | 34 | 1893 | 199.3258 | 0 |
| Token id값 사용 | 5000 | 125 | 7 | 382 | 53.45967 | 0 |


</div>
</details>

<br>

## 🤸🏻‍ 팀원 | TEAM

|  ![kimhun](https://cdn.discordapp.com/attachments/457223932244656128/1026470579328520222/Untitled-3_0001_Layer-5.png) | ![RIM](https://cdn.discordapp.com/attachments/457223932244656128/1026470580913971200/Untitled-3_0000_Layer-4.png)  | ![yelim](https://cdn.discordapp.com/attachments/457223932244656128/1026470579764736050/Untitled-3_0002_Layer-6.png) | ![suweon](https://cdn.discordapp.com/attachments/457223932244656128/1026470580138016839/Untitled-3_0003_Layer-7.png) | ![yongwon](https://cdn.discordapp.com/attachments/457223932244656128/1026470580473577483/Untitled-3_0004_Layer-3.png) |![YURI](https://cdn.discordapp.com/attachments/457223932244656128/1026476377265930301/Untitled-3_0000_Layer-5.png) |
|--|--|--|--|--|--|
|  김 훈| 김휘림  | 김예림  | 박수원 | 박용원 | 김유리 |
| BE(리더) | BE | FE(부리더) | FE | FE | DESINGER |
| [🔗](https://github.com/hunkim00) | [🔗](https://github.com/kimilm)| [🔗](https://github.com/97yelim) | [🔗](https://github.com/kksltv123) | [🔗](https://github.com/ParkYongWon) |[🔗](http://yurikim.net) |
