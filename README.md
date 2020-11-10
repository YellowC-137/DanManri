# CAP

단국대 분실물 어플

10/7 일전까지
-Firebase 연동 O
-Google Map API 연동
-게시판 + 전체 UI 만들기 O
-분실과 습득 게시판 매칭 기능 구현과정 생각

유튜브 안드로이드 스튜디오
https://www.youtube.com/playlist?list=PLG7te9eYUi7sjJzJR2i5m6wv-X_7K2pVE : 기초강의
https://www.youtube.com/playlist?list=PLva6rQOdsvQXdSBN1r2mEt_tqES6NjKKj : 개별강의

FireBase 문서
https://firebase.google.com/docs/android/setup

구글맵 문서
https://developers.google.com/maps/documentation/android-sdk/overview?hl=ko

안드로이드 문서
https://developer.android.com/docs?hl=ko

임시로 FifeBase console 연결한것
https://console.firebase.google.com/project/lostnfound-3024f/overview

구글맵 API 연결, 지도에 마커 표시
https://mailmail.tistory.com/category/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C/9.%20%EA%B5%AC%EA%B8%80%EB%A7%B5%20API

firebase 기반 채팅
https://medium.com/@nanyoung18/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%8A%A4%ED%8A%9C%EB%94%94%EC%98%A4-%EA%B8%B0%EC%B4%88-6-%EC%B1%84%ED%8C%85-%EC%95%B1-%EB%A7%8C%EB%93%A4%EA%B8%B0-97c8f83e5bdf

구글맵은 일단 내 계정으로 만들기는 했으나 (체험판)
문서를 좀더 찾아보고 제대로 연결해야 할듯

로그인 인증 - 마이크로소프트 학교계정 , 전화인증(?), 학생증 

비밀번호/ 찾기?
ID는 학번@dankook.ac.kr 고정?

Fragment가 아닌 그냥 Activity로 구현하기?


10.05
로그인시 id,pw 경우 설정 해야함
슬라이드뷰가 아닌 버튼으로 lost,found 설정?
progressbar 설정 - 전역

Warning:(23, 9) Hardcoded string "Notify page", should use `@string` resource
Warning:(31, 18) Casting 'findViewById(...)' to 'EditText' is redundant
Warning:(24, 17) 'androidx.lifecycle.ViewModelProviders' is deprecated
몇몇 warning들 확인

10/06
Fragment - Home(게시판),notify(알림?-추후 수정예정),map,setting fragment
스크롤기능 X : 없어도 되는지 모르겠음, 추후에 추가
HomeActivity crash
게시판 글쓰기 까지 구현, 게시판 글모음 보기 X

11.03
설정 - 로그아웃, 회원탈퇴 추가

11.11
알림 - 키워드 추가 액티비티
KeywordBack.java
-> 시작할 때 키워드를 입력받고 새 포스트의 제목 혹은 내용이 키워드와 일치할 시 알림 발생
-> 포스트가 자신이 입력한 것일 경우 종료 후 다시 시작
-> 키워드가 중간에 변경될 시 다시 시작되게 변경해야 될 듯

HomeActivity에서 서비스 (KeywordBack.java) 실행
-> 나중에 SharedPreferences 이용해서 On / Off 가능하게 바꿈

===============전문가 자문 관련=================
안녕하세요?
SW중심대학사업단의 신영옥입니다.

전문가 자문 절차 및 전문가 소개 메일 보냅니다.

1. 전문가 자문 절차

    1) 자문 미팅 관련 
        - 전문가에게 '알리미'팀에 대한 내용 기 전달 되어 있음. ( 제안서, 팀명단 )
        - 팀장이 전문가에게 전화하여 미팅 일정을 잡는다. (오프라인, 온라인 미팅 모두 가능.)
           a. 오프라인 미팅
               . 미팅 장소는 단국대 내부/외부 상관 없음.
               . 단국대 내부 미팅시 장소가 여의치 않으면 SW ICT관 B103에서 미팅 가능함.
               . 회의실 앞 "회의실 예약표" 에 기입하여 예약하면 됨.)
            b. 온라인 미팅
                . ZOOM을 이용한 원격 미팅 가능.
                . 미팅 일자 및 시각을 요청하면, ZOOM 접속 ID/PW를 알려 줌.

    2) 보고서 관련
        - 1차 미팅 후 2주 이내 "산학협력 프로젝트 1차 결과 보고서" 메일로 제출
        -  프로젝트 종료 시점(학기말)에 "산학협력 프로젝트 2차 결과 보고서" 메일로 제출

2. 전문가 소개

    <장용국 책임연구원>
        -  LG전자의 DTX(Digital Transformation Task)센터 빅데이터실에서 근무
        -  LG인화원 경력사원 DX실습 코칭 중
        -  단국대학교에서 전자계산학을 전공.
        - 주요 경력
          Google과 VR/AR 검토 및 개발
          Qualcomm POC(Point of Contact) - 신규 Chipser 적용,제품기획, 개발
          MMS/SMS Library, Mobile Message DB 설계 및 제품 적용 경력이 있음.
          산업통상자원부장관상 표창(지식서비스산업융합발전, 2019/12)
        
         - 메일 주소 : roy.jang@lge.com   royjang09@gmail.com
         - 연락처 : 010-2327-5647
 
  궁금한 사항 있으면, 언제든지 연락 주세요.
  ==================================================================
