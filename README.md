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


11/02
WirtingActivity 업데이트.
파이어베이스 Realtime Database 를 이용한 게시글 올리기.

<추가된 패키지와 자바파일>
database -> FirebaseID
adapters -> PostAdapter
models -> Post
=============================
Post Adapter는 LostFragment위에 게시글 형태로 올라가는 java파일
Post는 Post Adapter에 올라가는 Title, contents 등을 get하기위한 것들
FirebaseID는 WirtringActivity에서 put한 데이터를 저장해 놓고 이를 LostFragment에서 가져와 쓴다.




RegActivity에 자동 로그인 코드 작성 후  주석처리 해놓음


아직 Lost Fragment와 글 확인은 미구현

